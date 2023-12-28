package com.example.bluetoothdevicelistandapicall.userinterface

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.registerReceiver
import com.example.bluetoothdevicelistandapicall.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var btPermission = false
/**
 * A simple [Fragment] subclass.
 * Use the [BluetoothListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BluetoothListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

  override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      val view = inflater.inflate(R.layout.fragment_bluetooth_list, container, false)

      textView = view.findViewById<TextView>(R.id.device_name)
      view.findViewById<Button>(R.id.scan_btn).setOnClickListener {
          scanBt()
      }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanBt()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BluetoothListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BluetoothListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun scanBt() {
        val bluetoothManager: BluetoothManager? =
            requireContext().getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
            }else{
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)

            }
        }
    }



    private val blueToothPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        isGranted:Boolean->
        if(isGranted){
            val bluetoothManager: BluetoothManager =requireContext().getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            btPermission = true
            if(bluetoothAdapter?.isEnabled == false){
                val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBTIntent)
            }else{
                scanBTD()
            }
        }
    }

   private val btActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
       result:ActivityResult ->
       if(result.resultCode == RESULT_OK){
           scanBTD()
       }
   }

    @SuppressLint("MissingPermission")
    private fun scanBTD(){
        val bluetoothManager: BluetoothManager =requireContext().getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView:View = inflater.inflate(R.layout.scan_bt,null)
        builder.setCancelable(false)
        builder.setView(dialogView)
        val btlst = dialogView.findViewById<ListView>(R.id.bt_lst)
        val dialog = builder.create()
        val pairedDevices:Set<BluetoothDevice> = bluetoothAdapter?.bondedDevices as Set<BluetoothDevice>
        val ADAhere:SimpleAdapter
        var data: MutableList<Map<String?, Any?>?>? = null
        data = ArrayList()
        if(pairedDevices.isNotEmpty())
        {
            val datanum1: MutableMap<String?, Any?> = HashMap()
            datanum1["A"] = ""
            datanum1["B"] = ""
            data.add(datanum1)
            for (device in pairedDevices){
                val datanum:MutableMap<String?,Any?> = HashMap()
                datanum["A"] = device.name
                datanum["B"] = device.address
                data.add(datanum)
            }

            val fromhere = arrayOf("A")
            val viewwhere = intArrayOf(R.id.item_name)
            ADAhere  = SimpleAdapter(requireContext(),data,R.layout.bt_item_list,fromhere,viewwhere)
            btlst.adapter = ADAhere
            ADAhere.notifyDataSetChanged()
            btlst.onItemClickListener = AdapterView.OnItemClickListener{adapterView, view, position, l ->
                val string = ADAhere.getItem(position) as HashMap<*, *>
                val deviceName = string["A"]
                textView.text = deviceName.toString()
//                binding.deviceName.text = deviceName
                dialog.dismiss()


            }
        }else{
            val value = "No Devices found"
            Toast.makeText(requireContext(),value,Toast.LENGTH_LONG).show()
            return
        }
        dialog.show()
    }

}