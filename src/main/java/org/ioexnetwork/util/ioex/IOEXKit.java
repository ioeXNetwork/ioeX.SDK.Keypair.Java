/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.ioexnetwork.util.ioex;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.ioexnetwork.api.Basic;
import org.ioexnetwork.common.ErrorCode;
import org.ioexnetwork.ioex.*;
import org.ioexnetwork.util.JsonUtil;

import javax.xml.bind.DatatypeConverter;
import java.util.LinkedHashMap;

/**
 * clark
 * <p>
 * 9/27/18
 */
public class IOEXKit {

    public static String genRawTransaction(JSONObject inputsAddOutpus) {
        try {
            JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject)transaction.get(0);
            JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");
            JSONArray outputs = json_transaction.getJSONArray("Outputs");
            UTXOTxInput[] utxoTxInputs = (UTXOTxInput[])Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            TxOutput[] txOutputs = (TxOutput[])Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);
            PayloadRegisterIdentification payload = null;
            if(json_transaction.has("Payload")){
                payload = JsonUtil.jsonStr2Entity(json_transaction.getString("Payload"),PayloadRegisterIdentification.class);
                String privKey = payload.getIdPrivKey();
                String address = ioeX.getAddressFromPrivate(privKey);
                String programHash = DatatypeConverter.printHexBinary(Util.ToScriptHash(address));
                payload.setProgramHash(programHash);
            }
            boolean bool = json_transaction.has("Memo");
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap();
            new RawTx("", "");
            if (payload != null && bool) {
                return ErrorCode.ParamErr("PayloadRecord And Memo can't be used at the same time");
            } else {
                RawTx rawTx;
                if (payload == null && bool == false) {
                    rawTx = ioeX.makeAndSignTx(utxoTxInputs, txOutputs);
                } else if (bool ) {
                    String memo = json_transaction.getString("Memo");
                    rawTx = ioeX.makeAndSignTx(utxoTxInputs, txOutputs, memo);
                } else {
                    rawTx = ioeX.makeAndSignTx(utxoTxInputs, txOutputs, payload);
                }

                resultMap.put("rawTx", rawTx.getRawTxString());
                resultMap.put("txHash", rawTx.getTxHash());
                return Basic.getSuccess("genRawTransaction", resultMap);
            }
        } catch (Exception var12) {
            return var12.toString();
        }
    }

    /**
     * check if address is valid.
     * @param addr
     * @return
     */
    public static boolean checkAddress(String addr) {
        return Util.checkAddress(addr);
    }

    /**
     * get Identity address from public key
     * @param publicKey
     * @return
     */
    public static String getIdentityFromPublicKey(String publicKey) {

        byte[] pub = DatatypeConverter.parseHexBinary(publicKey);

        byte[] rs = Util.CreateSingleSignatureRedeemScript(pub,3);

        byte[] ph = Util.ToCodeHash(rs,3);

        return Util.ToAddress(ph);
    }

}
