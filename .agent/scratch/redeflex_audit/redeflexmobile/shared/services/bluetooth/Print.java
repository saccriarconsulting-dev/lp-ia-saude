package com.axys.redeflexmobile.shared.services.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Message;
import android.widget.Toast;

import com.axys.redeflexmobile.shared.util.Alerta;

/**
 * Created by joao.viana on 13/07/2017.
 */

public class Print {
    public BluetoothService mService = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private Context mContext;

    public static final String THAI = "CP874";

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;

    private String mConnectedDeviceName = null;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public void sendDataByte(byte[] data) {
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Alerta alerta = new Alerta(mContext, "Erro", "Impressora não conectada");
            alerta.show();
            return;
        }
        mService.write(data);
    }

    public boolean isBluetoothEnable() {
        if (mBluetoothAdapter.isEnabled()) {
            return true;
        }
        return false;
    }

    public boolean isConnect() {
        if (mService.getState() == BluetoothService.STATE_CONNECTED) {
            return true;
        }
        return false;
    }

    private final android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(mContext, "Impressora Conectada", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast.makeText(mContext, "Conectando sua impressora...", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:

                    break;
                case MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(mContext, "Conectado a " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(mContext, msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_CONNECTION_LOST:
                    Toast.makeText(mContext, "Sua conexão com impressora foi perdida", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_UNABLE_CONNECT:
                    Toast.makeText(mContext, "Não foi possível conectar sua impressora, Tente novamente", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public Print(Context _Context) {
        mContext = _Context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mService = new BluetoothService(mContext, mHandler);
    }

    public void connect(String address) {
        if (BluetoothAdapter.checkBluetoothAddress(address)) {
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            mService.connect(device);
        } else {
            Alerta alerta = new Alerta(mContext, "Erro", "Não foi encontrada a impressora");
            alerta.show();
        }
    }
}