

package com.starlead.starleadhprecision.Service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.starlead.starleadhprecision.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothService {

    private static BluetoothService sBluetoothService;

    // Debugging
    private static final String TAG = "BluetoothService";

    // Name for the SDP record when creating server socket
    private static final String NAME = "BluetoothAssistant";

    private static UUID MY_UUID;


    private BluetoothAdapter mAdapter;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private BluetoothSocket mSocket;
    private BufferedReader mInStream;
    private OutputStream mOutStream;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_READ:
                    String res = msg.obj.toString();
                    if ("".equals(res)) {
                        return;
                    }
                    int i = res.indexOf("$");
                    if (i > 0) {
                        res = res.substring(i);
                    }
                    if (mCurrentListenter != null) {
                        mCurrentListenter.haveGPSData(res);
                    }
                    break;
                case Constants.MESSAGE_STATE_CHANGE:
                    mCurrentListenter.UpdateStatus(msg.arg1);
            }
        }
    };

    private int mState;
    private int mNewState;
    private boolean isStop = false;

    private LinkedList<BluetoothServiceListenter> mListenterList = new LinkedList<>();
    private BluetoothServiceListenter mCurrentListenter = null;
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_CONNECTFAILED = 1;
    public static final int STATE_CONNECTSUCCESS = 2;
    public static final int STATE_CONNECTING = 3; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 4;  // now connected to a remote device
    public static final int STATE_CONNECTSTOP = 5;
    public static final int STATE_CONNECTPAUSE = 6;


    public static BluetoothService getInstance(BluetoothServiceListenter listenter) {
        if (sBluetoothService == null) {
            sBluetoothService = new BluetoothService();
        }
        sBluetoothService.AttachListenter(listenter);
        return sBluetoothService;
    }

    public void DetachListenter() {
        if (mListenterList.isEmpty()) {
            if (mCurrentListenter == null) {
                stop();
            } else {
                mCurrentListenter = null;
            }
        } else {
            mCurrentListenter = mListenterList.pop();
        }
    }

    private void AttachListenter(BluetoothServiceListenter listenter) {
        if (mCurrentListenter != null) {
            mListenterList.push(mCurrentListenter);
        }
        mCurrentListenter = listenter;
    }

    public synchronized void connect(BluetoothDevice device) {
        if (!isPaired(device)) {
            device.createBond();
            return;
        }

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }


        mConnectThread = (ConnectThread) new ConnectThread(device);
        mConnectThread.start();
    }

    public synchronized void write(String buffer) {

        if (mState != STATE_CONNECTED)
            return;
        mConnectedThread.write(buffer.getBytes());

    }

    public synchronized void pause() {
        isStop = true;
    }

    public synchronized void again() {
        isStop = false;
    }

    public synchronized void stop() {
        mState = STATE_CONNECTSTOP;
        updateUserUIToStauts();
    }

    public synchronized int getState() {
        return mState;
    }


    private BluetoothService() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
    }


    private synchronized void connected(BluetoothSocket socket) {
        init();
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
    }


    /**
     * Update UI title according to the current state of the chat connection
     */
    private synchronized void updateUserUIToStauts() {
        mState = getState();
        if (mCurrentListenter != null) {
            mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, mState, -1).sendToTarget();
        }
    }


    private synchronized void init() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
    }

    private synchronized boolean isPaired(BluetoothDevice device) {
        Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice d : pairedDevices) {
                if (device.getAddress().equals(d.getAddress())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        mState = STATE_CONNECTFAILED;
        updateUserUIToStauts();
        mState = STATE_NONE;
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        mState = STATE_NONE;
        // Start the service over to restart listening mode
        BluetoothService.this.init();
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {

        private boolean Connect(BluetoothSocket Socket) {
            try {
                Socket.connect();
                mState = STATE_CONNECTSUCCESS;
            } catch (IOException e) {
                BluetoothSocket socket = FallbackBluetoothSocket(mSocket);
                try {
                    Thread.sleep(500);
                    socket.connect();
                    mSocket = socket;
                    mState = STATE_CONNECTSUCCESS;
                    return true;
                } catch (Exception e1) {
                    Log.i(TAG, "Connect: ", e);
                    mState = STATE_CONNECTFAILED;
                    return false;
                }
            }
            return true;

        }

        private BluetoothSocket createBluetoothSocket(BluetoothDevice device)
                throws IOException {
            try {
                MY_UUID = device.getUuids()[0].getUuid();
            } catch (Exception e) {
                MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                if (Build.VERSION.SDK_INT >= 10) {
                    try {
                        final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                        return (BluetoothSocket) m.invoke(device, MY_UUID);
                    } catch (Exception e1) {
                        Log.e(TAG, "Could not create Insecure RFComm Connection", e1);
                    }
                }
            }

            return device.createRfcommSocketToServiceRecord(MY_UUID);
        }

        private BluetoothSocket FallbackBluetoothSocket(BluetoothSocket tmp) {
            BluetoothSocket socket = null;
            try {
                socket = (BluetoothSocket) tmp.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(tmp, 1);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return socket;

        }

        public ConnectThread(BluetoothDevice device) {

            try {
                mSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                connectionFailed();
            }
            mState = STATE_CONNECTING;
            mAdapter.cancelDiscovery();
            updateUserUIToStauts();
        }


        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                if (Connect(mSocket)) {
                    break;
                }
            }

            if (mState != STATE_CONNECTSUCCESS) {
                try {
                    mSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close()  socket during connection failure", e2);
                }
                connectionFailed();
            }


            // Reset the ConnectThread because we're done
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }

            if (mSocket != null && mSocket.isConnected()) {
                connected(mSocket);
            }
        }


        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect  socket failed", e);
            }
        }


    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {


        public ConnectedThread(BluetoothSocket socket) {
            mSocket = socket;
            OutputStream tmpOut = null;
            InputStream tmpIn = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpOut = mSocket.getOutputStream();
                mInStream = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "GBK"));
                tmpIn = mSocket.getInputStream();
            } catch (IOException e) {
                mState = STATE_CONNECTFAILED;
                updateUserUIToStauts();
                return;
            }

//            mInStream = tmpIn;
            mOutStream = tmpOut;
            mState = STATE_CONNECTSUCCESS;
            updateUserUIToStauts();
            mState = STATE_CONNECTED;
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }

        @Override
        public void run() {
            while (mState == STATE_CONNECTED) {

                try {
                    if (!isStop) {
                        String res = mInStream.readLine();
                        mHandler.obtainMessage(Constants.MESSAGE_READ, -1, -1, res)
                                .sendToTarget();
                    }
                } catch (IOException e) {
                    connectionLost();
                    break;
                }
            }
            if (mState == STATE_CONNECTSTOP) {
                init();
            }
        }


    }
}
