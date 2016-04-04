package br.com.sangueheroi.sangueheroi.network;

/**
 * Created by Diego Lucas on 03/04/2016.
 */
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import br.com.sangueheroi.sangueheroi.model.Usuario;

/**
 * Created by Diego Lucas on 03/04/2016.
 */
public interface ContractWS{
    final String   SOAP_ACTION = "http://sangueheroiweb.azurewebsites.net/WebService.asmx/";
    final String   URL         = "http://sangueheroiweb.azurewebsites.net/WebService.asmx";

    public SoapPrimitive callServiceLogin(Usuario usuario);

    public SoapPrimitive callServiceCadastraUsuario(Usuario usuario);



}
