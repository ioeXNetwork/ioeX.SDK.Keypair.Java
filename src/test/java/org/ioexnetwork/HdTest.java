/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.ioexnetwork;

import io.github.novacrypto.bip32.Index;
import org.ioexnetwork.ioex.ioeX;
import org.ioexnetwork.entity.HdWalletEntity;
import org.ioexnetwork.entity.MnemonicType;
import org.ioexnetwork.util.JsonUtil;
import org.ioexnetwork.util.ioex.IOEXHdSupport;
import org.ioexnetwork.util.ioex.IOEXSignTool;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;

/**
 * clark
 * <p>
 * 10/17/18
 */
public class HdTest {

    @Test
    public void test01() throws Exception{
        String mnemonic = IOEXHdSupport.generateMnemonic(MnemonicType.CHINESE);
        System.out.println(mnemonic);
        String wallet   = IOEXHdSupport.generate(mnemonic,0);
        String wallet1  = IOEXHdSupport.generate(mnemonic,1);
        String wallet2  = IOEXHdSupport.generate(mnemonic,2);
        System.out.println(wallet +"\n" + wallet1 +"\n" + wallet2);
    }

    @Test
    public void test02(){

        String str = "{\"mnemonic\":\"salute afford few tackle feel right sea member shop choose borrow mosquito\",\"index\":10}";

        HdWalletEntity entity = JsonUtil.jsonStr2Entity(str,HdWalletEntity.class);
        Integer i = entity.getStart();
        System.out.println();
    }

    /**
     * {
     *     "result": {
     *         "msg": "68656C6C6F776F726C64",
     *         "pub": "028971D6DA990971ABF7E8338FA1A81E1342D0E0FD8C4D2A4DF68F776CA66EA0B1",
     *         "sig": "7F0D913A08E0DF7A4CA6012621AC52D7B1EA4B88CD58BB1000C4E1F66848B023431CB2409B7F2B34CD1CECF7AD4D37B3ED56C4E6E66BA7640182323C2F92A61D"
     *     },
     *     "status": 200
     * }
     */
    @Test
    public void test03(){
        String msg = "68656C6C6F776F726C64";
        String pub = "028971D6DA990971ABF7E8338FA1A81E1342D0E0FD8C4D2A4DF68F776CA66EA0B1";
        String sig = "7F0D913A08E0DF7A4CA6012621AC52D7B1EA4B88CD58BB1000C4E1F66848B023431CB2409B7F2B34CD1CECF7AD4D37B3ED56C4E6E66BA7640182323C2F92A61D";
        boolean b = IOEXSignTool.verify(DatatypeConverter.parseHexBinary(msg),DatatypeConverter.parseHexBinary(sig),DatatypeConverter.parseHexBinary(pub));
        System.out.print(b);
    }

    @Test
    public void test04() throws Exception{
        String mnemonic = "obtain pill nest sample caution stone candy habit silk husband give net";
        String wallet = IOEXHdSupport.generate(mnemonic,1);
        System.out.println(wallet);
        String str = "AAAAAAAAAAB1131FE85FC597E6116E0E0C7B0F2ED299D991946C935DF567FE34";
        String str1 = ioeX.getPublicFromPrivate(str);
        String str2 = ioeX.getAddressFromPrivate(str);
        System.out.println(str1 + " " +str2);
    }

}
