package com.intermeet.android
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.Instant
import java.time.ZoneId
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
    private lateinit var hangoutName: EditText
    private lateinit var hangoutTimeBegin: TextView
    private lateinit var hangoutTimeEnd: TextView
    private lateinit var hangoutLocation: EditText
    private lateinit var hangoutDescription: EditText
    private lateinit var saveEventButton: Button
    private lateinit var stringDateSelected: String
    private lateinit var addHangoutButton: ImageButton
    private lateinit var timePickerSpinner: TimePicker
    private lateinit var currentHangoutName: TextView
    private lateinit var currentHangoutTime: TextView
    private lateinit var currentHangoutLocation: TextView
    private lateinit var currentHangoutDescription: TextView

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
        currentHangoutName = dialog.findViewById(R.id.selected_hangout_name)
        currentHangoutTime = dialog.findViewById(R.id.selected_hangout_time)
        currentHangoutLocation = dialog.findViewById(R.id.selected_hangout_location)
        currentHangoutDescription = dialog.findViewById(R.id.selected_hangout_description)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            stringDateSelected = "$year-${month + 1}-$dayOfMonth"
            Log.d("Calendar", stringDateSelected)
            calendarClicked()
        }

        addHangoutButton.setOnClickListener {
            showAddHangout()
        }

        dialog.show()
    }

    private fun calendarClicked() {
        mDbRef.child("chats").child(senderRoom!!).child("calendar").child(stringDateSelected).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hangout = snapshot.getValue(Hangout::class.java)
                if (hangout != null) {
                    currentHangoutName.text = hangout.name
                    currentHangoutName.visibility = View.VISIBLE
                    currentHangoutTime.text = "${hangout.beginTime} - ${hangout.endTime}"
                    currentHangoutTime.visibility = View.VISIBLE
                    currentHangoutLocation.text = hangout.location
                    currentHangoutLocation.visibility = View.VISIBLE
                    currentHangoutDescription.text = hangout.description
                    currentHangoutDescription.visibility = View.VISIBLE
                }
                else {
                    currentHangoutName.visibility = View.GONE
                    currentHangoutTime.visibility = View.GONE
                    currentHangoutLocation.visibility = View.GONE
                    currentHangoutDescription.visibility = View.GONE

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun buttonSaveEvent(hangout: Hangout) {
        mDbRef.child("chats").child(senderRoom!!).child("calendar").child(stringDateSelected)
            .setValue(hangout).addOnSuccessListener {
                mDbRef.child("chats").child(receiverRoom!!).child("calendar").child(stringDateSelected)
                    .setValue(hangout)
            }
    }

    private fun showAddHangout() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.add_hangout_view)
        hangoutName = dialog.findViewById(R.id.hangout_name_text)
        hangoutTimeBegin = dialog.findViewById(R.id.hangout_time_begin_text)
        hangoutTimeEnd = dialog.findViewById(R.id.hangout_time_end_text)
        hangoutLocation = dialog.findViewById(R.id.hangout_location_text)
        hangoutDescription = dialog.findViewById(R.id.hangout_description_text)
        saveEventButton = dialog.findViewById(R.id.save_event_button)

        hangoutTimeBegin.setOnClickListener {
            showTimePickerDialog("begin")
        }

        hangoutTimeEnd.setOnClickListener {
            showTimePickerDialog("end")
        }

        saveEventButton.setOnClickListener {
            val hangout = Hangout(
                hangoutName.getText().toString(),
                hangoutTimeBegin.getText().toString(),
                hangoutTimeEnd.getText().toString(),
                hangoutLocation.getText().toString(),
                hangoutDescription.getText().toString()
            )

            buttonSaveEvent(hangout)

            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("DefaultLocale")
    private fun showTimePickerDialog(beginOrEnd: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.time_spinner_view)

        timePickerSpinner = dialog.findViewById(R.id.time_spinner)
        timePickerSpinner.setIs24HourView(false)
        timePickerSpinner.setOnTimeChangedListener { view, hourOfDay, minute ->
            var selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            if(beginOrEnd == "begin") {
                if(hourOfDay < 12) {
                    hangoutTimeBegin.text = "${selectedTime} AM"
                }
                else {
                    val modifiedHour = hourOfDay - 12
                    selectedTime = String.format("%02d:%02d", modifiedHour, minute)
                    hangoutTimeBegin.text = "${selectedTime} PM"
                }
            }
            else {
                if(hourOfDay < 12) {
                    hangoutTimeEnd.text = "- ${selectedTime} AM"
                }
                else {
                    val modifiedHour = hourOfDay - 12
                    selectedTime = String.format("%02d:%02d", modifiedHour, minute)
                    hangoutTimeEnd.text = "- ${selectedTime} PM"
                }
            }
        }
        dialog.show()
    }
}
