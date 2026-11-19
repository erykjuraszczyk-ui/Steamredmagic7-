package pl.steamdroid.app

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.InputDevice

data class NetworkDiagnostics(
    val connected: Boolean,
    val transportName: String,
    val hasInternetCapability: Boolean,
    val internetValidated: Boolean,
    val isMetered: Boolean?,
    val note: String
)

data class ControllerDiagnostics(
    val name: String,
    val inputType: String
)

data class DeviceDiagnostics(
    val network: NetworkDiagnostics,
    val steamLinkInstalled: Boolean,
    val controllers: List<ControllerDiagnostics>
) {
    companion object {
        private const val STEAM_LINK_PACKAGE = "com.valvesoftware.steamlink"

        fun read(context: Context): DeviceDiagnostics {
            return DeviceDiagnostics(
                network = readNetwork(context),
                steamLinkInstalled = context.packageManager
                    .getLaunchIntentForPackage(STEAM_LINK_PACKAGE) != null,
                controllers = readControllers()
            )
        }

        private fun readNetwork(context: Context): NetworkDiagnostics {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = manager.activeNetwork
            val capabilities = network?.let(manager::getNetworkCapabilities)

            if (network == null || capabilities == null) {
                return NetworkDiagnostics(
                    connected = false,
                    transportName = "Brak aktywnej sieci",
                    hasInternetCapability = false,
                    internetValidated = false,
                    isMetered = null,
                    note = "Android nie zgłasza obecnie aktywnego połączenia sieciowego."
                )
            }

            val transport = when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Sieć komórkowa"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "Bluetooth"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
                else -> "Inne połączenie"
            }

            val hasInternet = capabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
            val validated = capabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_VALIDATED
            )
            val metered = !capabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_NOT_METERED
            )

            val note = when {
                validated -> "Połączenie ma zweryfikowany dostęp do internetu."
                hasInternet -> "Sieć deklaruje dostęp do internetu, ale Android nie potwierdził łączności."
                else -> "Połączenie jest aktywne, ale Android nie zgłasza dostępu do internetu."
            }

            return NetworkDiagnostics(
                connected = true,
                transportName = transport,
                hasInternetCapability = hasInternet,
                internetValidated = validated,
                isMetered = metered,
                note = note
            )
        }

        private fun readControllers(): List<ControllerDiagnostics> {
            return InputDevice.getDeviceIds()
                .map { id -> InputDevice.getDevice(id) }
                .filterNotNull()
                .filterNot { it.isVirtual }
                .mapNotNull { device ->
                    val hasGamepad = (device.sources and InputDevice.SOURCE_GAMEPAD) ==
                        InputDevice.SOURCE_GAMEPAD
                    val hasJoystick = (device.sources and InputDevice.SOURCE_JOYSTICK) ==
                        InputDevice.SOURCE_JOYSTICK
                    val hasDpad = (device.sources and InputDevice.SOURCE_DPAD) ==
                        InputDevice.SOURCE_DPAD

                    if (!hasGamepad && !hasJoystick && !hasDpad) {
                        null
                    } else {
                        val type = when {
                            hasGamepad && hasJoystick -> "Gamepad + joystick"
                            hasGamepad -> "Gamepad"
                            hasJoystick -> "Joystick"
                            else -> "D-pad"
                        }
                        ControllerDiagnostics(
                            name = device.name.ifBlank { "Urządzenie wejściowe" },
                            inputType = type
                        )
                    }
                }
                .distinctBy { it.name to it.inputType }
        }
    }
}
