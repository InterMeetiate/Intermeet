package com.intermeet.android
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


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

    var receiverRoom: String? = null
    var senderRoom: String? = null

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
}
