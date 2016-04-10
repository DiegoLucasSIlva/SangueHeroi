package br.com.sangueheroi.sangueheroi.network;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import br.com.sangueheroi.sangueheroi.model.Usuario;

/**
 * Created by Diego Lucas on 03/04/2016.
 */
public class RequestWS implements  ContractWS{

    private final String TAG = "LOG";
    SoapPrimitive result;

    public RequestWS(){

    }
    @Override
    public SoapPrimitive callServiceLogin(Usuario usuario) {
        String  METHOD_NAME = "efetuarLogin";
        try {
            Log.d(TAG, "callServiceLogin "+usuario.toString());

            SoapObject Request = new SoapObject(URL, METHOD_NAME);
            Request.addProperty("login",usuario.getNome());
            Request.addProperty("senha",usuario.getSenha());

            result = callServiceGeneric(Request, METHOD_NAME);
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
        return result;
    }

    @Override
    public SoapPrimitive callServiceCadastraUsuario(Usuario usuario) {
        String  METHOD_NAME = "registrarUsuario";
        Log.d(TAG, "callServiceCadastraUsuario "+usuario.toString());

        try {
            SoapObject Request = new SoapObject(URL, METHOD_NAME);
            Request.addProperty("nome", usuario.getNome());
            Request.addProperty("email", usuario.getEmail());
            Request.addProperty("senha", usuario.getSenha());
            Request.addProperty("logradouro", "Rua dos blbla");
            Request.addProperty("bairro", "Rua dos blbla");
            Request.addProperty("cidade", "Rua dos blbla");
            Request.addProperty("estado", "Rua dos blbla");
            Request.addProperty("cep", "Rua dos blbla");
            Request.addProperty("tipo_sanguineo", usuario.getTipo_sanguineo());
            Request.addProperty("dtnascimento", "11/02/2016");
            Request.addProperty("dtultimadoacao", "11/02/2016");
            Request.addProperty("codigo_heroi ", "1");

            result = callServiceGeneric(Request, METHOD_NAME);
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
        return result;
    }

    private SoapPrimitive callServiceGeneric(SoapObject Request,String METHOD_NAME) {
        try {

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION + METHOD_NAME, soapEnvelope);
            result = (SoapPrimitive) soapEnvelope.getResponse();

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
        return result;
    }



    public static boolean verifyConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


}
