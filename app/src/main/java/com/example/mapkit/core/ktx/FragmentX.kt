package com.example.mapkit.core.ktx

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline val Fragment.screenWidth: Int
    get() = resources.displayMetrics.widthPixels


inline val Fragment.screenHeight: Int
    get() = resources.displayMetrics.heightPixels


fun <T : Any> argument(): ReadWriteProperty<Fragment, T> = FragmentArgumentDelegate()
fun <T : Any> argumentNullable(): ReadWriteProperty<Fragment, T?> =
    FragmentNullableArgumentDelegate()

class FragmentArgumentDelegate<T : Any> : ReadWriteProperty<Fragment, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(
        thisRef: Fragment,
        property: KProperty<*>
    ): T {
        val key = property.name
        return thisRef.arguments
            ?.get(key) as? T
            ?: throw IllegalStateException("Property ${property.name} could not be read")
    }

    override fun setValue(
        thisRef: Fragment,
        property: KProperty<*>, value: T
    ) {
        val args = thisRef.arguments ?: Bundle().also(thisRef::setArguments)
        val key = property.name
        args.put(key, value)
    }
}


class FragmentNullableArgumentDelegate<T : Any?> :
    ReadWriteProperty<Fragment, T?> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(
        thisRef: Fragment,
        property: KProperty<*>
    ): T? {
        val key = property.name
        return thisRef.arguments?.get(key) as? T
    }

    override fun setValue(
        thisRef: Fragment,
        property: KProperty<*>, value: T?
    ) {
        val args = thisRef.arguments ?: Bundle().also(thisRef::setArguments)
        val key = property.name
        value?.let { args.put(key, it) } ?: args.remove(key)
    }
}

fun <T> Bundle.put(key: String, value: T) {
    when (value) {
        is Boolean -> putBoolean(key, value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Short -> putShort(key, value)
        is Long -> putLong(key, value)
        is Byte -> putByte(key, value)
        is ByteArray -> putByteArray(key, value)
        is Char -> putChar(key, value)
        is CharArray -> putCharArray(key, value)
        is CharSequence -> putCharSequence(key, value)
        is Float -> putFloat(key, value)
        is Bundle -> putBundle(key, value)
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}

fun fragmentNavController(): FragmentNavControllerDelegate = FragmentNavControllerDelegate()
fun activityNavController(@IdRes viewId: Int): ActivityNavControllerDelegate =
    ActivityNavControllerDelegate(viewId)

class FragmentNavControllerDelegate : ReadOnlyProperty<Fragment, NavController> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): NavController {
        return thisRef.findNavController()
    }
}

class ActivityNavControllerDelegate(@IdRes private val viewId: Int) :
    ReadOnlyProperty<Fragment, NavController> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): NavController {
        return thisRef.requireActivity().findNavController(viewId)
    }
}

//fun Fragment.backPressedScreen(enabled: Boolean = false, handleOnBackPressed: () -> Unit) {
//    // This callback will only be called when MyFragment is at least Started.
//    val callback: OnBackPressedCallback =
//        object : OnBackPressedCallback(enabled /* enabled by default */) {
//            override fun handleOnBackPressed() {
//                // Handle the back button event
//                handleOnBackPressed.invoke()
//            }
//        }
//    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
//}

fun <T> Fragment.getNavigationResult(key: String = "RESULT") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "RESULT") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun <T> Fragment.getNavigationResultObserver(key: String = "RESULT", observer: Observer<T>) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
        ?.observe(viewLifecycleOwner, observer)

fun <T> Fragment.getNavigationResultSingleObserver(key: String = "RESULT", observer: Observer<T>) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
        ?.observe(this, observer)

fun Fragment.shareText(text: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun Fragment.copyText(text: String) {
    val myClipboard: ClipboardManager =
        requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val myClip = ClipData.newPlainText("text", text)
    myClipboard.setPrimaryClip(myClip)
}

fun Fragment.alertDialog(): AlertDialog.Builder {
    return AlertDialog.Builder(requireContext())
}

fun Fragment.chooseContact(block: (String) -> Unit) = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->

    if (result.resultCode == Activity.RESULT_OK) {
        result?.data?.data?.let {

            val contactId = getContactIdFromUri(it, requireActivity())
            val phones = getPhonesFromContactId(contactId, requireActivity())
            if (phones.isEmpty()) {
                Toast.makeText(context, "No Phone Number", Toast.LENGTH_SHORT)
                    .show()
            } else if (phones.size == 1) {
                block.invoke(phones[0])
            } else {
                val phonesArr = phones.toTypedArray()
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Choose one of them")
                builder.setItems(phonesArr) { dialog, which -> block.invoke(phonesArr[which]) }
                builder.show()
            }
        }
    }

}


private fun getContactIdFromUri(contactUri: Uri, activity: Activity): Long {
    val cur = activity.contentResolver?.query(
        contactUri,
        arrayOf(ContactsContract.Contacts._ID),
        null,
        null,
        null
    )
    var id: Long = -1
    if (cur != null) {
        if (cur.moveToFirst()) {
            id = cur.getLong(0)
        }
        cur.close()
    }
    return id
}

private fun getPhonesFromContactId(contactId: Long, activity: Activity): List<String> {
    val cur = activity.contentResolver?.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
        arrayOf(contactId.toString()),
        null
    )
    val phones = java.util.ArrayList<String>()
    while (cur!!.moveToNext()) {
        val phone = cur.getString(0)
        phones.add("️$phone")
    }
    cur.close()
    return phones
}