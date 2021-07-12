package com.example.blogapp.data.model.remote.home

import com.example.blogapp.core.Resource
import com.example.blogapp.data.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {

    suspend fun getLatestPosts() : Resource<List<Post>> {
        val postList = mutableListOf<Post>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("posts").get().await()
        for (post in querySnapshot.documents){
            post.toObject(Post::class.java)?.let { fbPost ->
                fbPost.apply { create_at = post.getTimestamp("create_at",DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)?.toDate()}
                postList.add(fbPost)
            }
        }
        return Resource.Success(postList)
    }
}