package com.sub6resources.frcscouting.form.fragments

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.View
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.model.Image
import com.sub6resources.frcscouting.form.recyclerviews.FormRecyclerAdapter
import com.sub6resources.frcscouting.form.viewmodels.FormViewModel
import com.sub6resources.utilities.*
import kotlinx.android.synthetic.main.fragment_form.*
import java.io.ByteArrayOutputStream
import java.util.UUID
import android.graphics.BitmapFactory




/**
 * Created by whitaker on 12/28/17.
 */
class FormFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_form

    val viewModel by getSharedViewModel(FormViewModel::class.java)
    val formRecycler by bind<RecyclerView>(R.id.form_recycler)

    var isEditing = false

    override fun onDestroy() {
        super.onDestroy()
    }

    val formAdapter by lazy {
        FormRecyclerAdapter(
                fields = listOf(),
                getChoicesForField = { field ->
                    viewModel.getChoicesForField(field.id)
                },
                setAnswer = { field, answer ->
                    viewModel.setAnswer(field, answer)
                },
                getAnswer = { field ->
                    viewModel.getAnswer(field)
                },
                getExistingImages = { field, callback ->
                    callback(viewModel.getImages(field).map<Image, Bitmap> {
                        val decodedString = Base64.decode(it.base64, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    })
                },
                selectImages = { field, callback ->
                    (activity as BaseActivity).startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE)) { resultCode, data ->
                        if (resultCode == RESULT_OK) {
                            val imageBitmap = data.extras.get("data") as Bitmap
                            val baos = ByteArrayOutputStream()
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val byteArrayImage = baos.toByteArray()
                            val encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT)

                            val count = viewModel.appendToAnswer(field, encodedImage)
                            //Callback the bitmap and the number of images.
                            callback(imageBitmap, count)
                        }
                    }
                }
        )
    }

    override fun onBackPressed() {
        baseActivity.dialog {
            title("Exit without submitting?")
            content("This form submission will be lost if you exit. Are you sure you want to exit?")
            positiveText("Okay")
            negativeText("Cancel")
            onPositive { _,_ ->
                if(!isEditing)
                    viewModel.deleteFormResponse()
                baseActivity.finish()
            }
        }.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        formRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        formRecycler.adapter = formAdapter

        baseActivity.intent?.let {
            viewModel.selectForm(it.extras["formId"] as UUID)
            viewModel.selectFormResponse(it.extras["formResponseId"] as UUID)
            isEditing = it.extras["editing"] != null
        }

        observeNotNull(viewModel.form) {
            quiz_toolbar.title = it.name
        }

        observeNotNull(viewModel.fields) {
            formAdapter.replaceData(it)
        }

        observeNotNull(viewModel.fieldResponses) {

        }

        //😢
        observeNotNull(viewModel.formResponse) {}

        button_submit.onClick {
            if(viewModel.validateFormCompleted()) {
                baseActivity.finish()
            } else {
                //Form is invalid TODO show an error message
            }
        }
    }

}