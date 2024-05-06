package com.intermeet.android
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class ChatActivity : AppCompatActivity() {


    private lateinit var receiverUid: String
    private lateinit var chatView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button
    private lateinit var profileImage: ImageView
    private lateinit var backButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var userName: TextView
    private lateinit var calenderIcon: ImageView
    private lateinit var calendarView: CalendarView
    private lateinit var eventNameText: EditText
    private lateinit var saveEventButton: Button
    private lateinit var stringDateSelected: String
    private lateinit var addHangoutButton: ImageButton

    var receiverRoom: String? = null
    var senderRoom: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatView = findViewById(R.id.chatView)
        messageBox = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.send_button)
        profileImage = findViewById(R.id.profile_image)
        messageList = ArrayList()
        userName = findViewById(R.id.user_name)
        backButton = findViewById(R.id.back_arrow)
        calenderIcon = findViewById(R.id.calendar_icon)
        messageAdapter = MessageAdapter(this, messageList)

        chatView.layoutManager = LinearLayoutManager(this)
        chatView.adapter = messageAdapter

        // Retrieve the userId extra from the Intent
        receiverUid = intent.getStringExtra("userId") ?: ""

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        fetchUserName(receiverUid)

        // Add data to recyclerView
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Message(message, senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }

        backButton.setOnClickListener {
            finish()
        }

        calenderIcon.setOnClickListener {
            showCalendarDialog()
        }
    }


    private fun fetchUserName(userId: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val firstName = snapshot.child("firstName").getValue(String::class.java) ?: ""
                val lastName = snapshot.child("lastName").getValue(String::class.java) ?: ""
                val photoDownloadUrls = snapshot.child("photoDownloadUrls").children.mapNotNull { it.getValue(String::class.java) }
                userName.text = "${firstName} ${lastName}"

                Glide.with(this@ChatActivity)
                    .load(photoDownloadUrls[0])
                    .circleCrop()
                    .into(profileImage)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showCalendarDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.calendar_view)

        calendarView = dialog.findViewById(R.id.calendar_view)
        addHangoutButton = dialog.findViewById(R.id.add_hangout_button)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            stringDateSelected = "$year-${month + 1}-$dayOfMonth"
            Log.d("Calendar", stringDateSelected)
            //calendarClicked()
        }

        addHangoutButton.setOnClickListener {
            showAddHangout()
        }

        dialog.show()
    }

//    private fun calendarClicked() {
//        mDbRef.child("chats").child(senderRoom!!).child(stringDateSelected).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.value != null) {
//                    editText.setText(snapshot.value.toString())
//                } else {
//                    editText.setText("null")
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle possible errors
//            }
//        })
//    }

    private fun buttonSaveEvent() {
        mDbRef.child("chats").child(senderRoom!!).child("calendar").child(stringDateSelected).child("hangoutName")
            .setValue(eventNameText.text.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun epochToDateString(epochMilli: Long, zoneId: String = "UTC"): String {
        val dateTime = Instant.ofEpochMilli(epochMilli).atZone(ZoneId.of(zoneId))
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return dateTime.format(formatter)
    }

    private fun showAddHangout() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.add_hangout_view)

        dialog.show()
    }
}
