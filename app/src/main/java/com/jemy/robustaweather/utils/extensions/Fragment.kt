package com.jemy.robustaweather.utils.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.rximagepicker.RxImagePicker
import com.google.android.material.snackbar.Snackbar
import com.jemy.robustaweather.R
import com.tbruyelle.rxpermissions2.RxPermissions
import id.zelory.compressor.Compressor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import rx.Observable
import java.io.File


fun Context.toast(msg: String?) {
    msg?.let {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }
}

fun Context.toastLong(msg: String?) {
    msg?.let {
        Toast.makeText(this, it, Toast.LENGTH_LONG).show()
    }
}

fun Fragment.hideKeypad() {
    activity?.let { activity ->
        activity.currentFocus?.let { view ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

fun Fragment.showKeypad() {
    activity?.let { activity ->
        val manager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
}

fun Fragment.loadingDialog(compositeDisposable: CompositeDisposable): Dialog? {
    return if (isAdded) {
        val progressDialog = Dialog(activity!!, R.style.TransDialog)
        progressDialog.setContentView(R.layout.loading_dialog)
        progressDialog.setCancelable(true)
        progressDialog.setOnCancelListener { compositeDisposable.clear() }
        progressDialog
    } else {
        null
    }
}

fun Fragment.messageDialog(@StringRes res: Int? = null, message: String? = null): MaterialDialog? {
    return if (isAdded) {
        return MaterialDialog(activity!!)
            .title(R.string.dialog_title)
            .message(res, message)
            .cancelable(true)
            .cancelOnTouchOutside(true)
            .positiveButton(android.R.string.ok) { it.dismiss() }
    } else {
        null
    }
}

fun Fragment.requestRxPermission(
    vararg permissions: String,
    compositeDisposable: CompositeDisposable,
    whenGranted: () -> Unit,
    whenDenied: () -> Unit
) {
    RxPermissions(this)
        .request(*permissions).subscribe { isGranted ->
            if (isGranted){
                whenGranted.invoke()
            }else{
                whenDenied.invoke()
            }
        }.addTo(compositeDisposable)
}

fun Fragment.showPermissionSnackBar(){
    Snackbar.make(this.requireView(), getString(R.string.perm_request_try_again), Snackbar.LENGTH_LONG)
        .setAction(getString(R.string.perm_settings)) { requireActivity().openPermSettings() }
        .show()
}

fun Fragment.openReactiveImagePicker(compress: Boolean = true, showCamera: Boolean = true, limit: Int = 1): Observable<List<File>?> {
    val imagePicker = ImagePicker.create(this)
        .folderMode(true)
        .returnMode(ReturnMode.CAMERA_ONLY)
        .toolbarArrowColor(Color.WHITE)
        .includeVideo(false)
        .multi()
        .limit(limit)
        .showCamera(showCamera)
        .imageDirectory("Camera")
        .theme(R.style.AppTheme)
        .enableLog(false)

    fun compressInBackground(path: String): File =
        Compressor(activity).compressToFileAsFlowable(File(path))
            .subscribeOn(Schedulers.io())
            .blockingSingle()

    return RxImagePicker.getInstance().start(activity, imagePicker)
        .map { images -> images?.let { it.map { image -> if (compress) compressInBackground(image.path) else File(image.path) } } }
}
