import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.fragment.app.FragmentManager
import com.example.mapkit.app.ui.dialog.NoConnectionBottomDialog

class NetworkChangeReceiver(private val fragmentManager: FragmentManager) : BroadcastReceiver() {
    private val noConnectionDialog = NoConnectionBottomDialog()

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            if (isNetworkAvailable(it)) {
                // Network is available
                try {
                    //noConnectionDialog.dismiss()
                    noConnectionDialog.updateNetworkState(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                // Network is not available
                try {
                    noConnectionDialog.show(fragmentManager)
                    noConnectionDialog.updateNetworkState(false)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}
