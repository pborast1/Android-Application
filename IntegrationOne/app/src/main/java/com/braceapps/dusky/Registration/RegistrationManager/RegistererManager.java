package com.braceapps.dusky.Registration.RegistrationManager;

import android.content.Context;
import android.text.TextUtils;

import com.braceapps.dusky.Registration.AppRegistration.ExpHandler;
import com.braceapps.dusky.Registration.AppRegistration.GCMRegisterer;
import com.braceapps.dusky.Registration.AppRegistration.RegistrationValidator;
import com.braceapps.dusky.Registration.InternalStorageRegistration.StorageRegistration;
import com.braceapps.dusky.Registration.RegistrationDetails.InfoRetriever;

/**
 * Created by Paresh on 1/10/2015.
 */
public class RegistererManager {
    public int registerAndStoreRegId(Context param_context) {
        int L_Status = 0;
        InfoRetriever objInfoRetriever = new InfoRetriever();
        int infostatus = objInfoRetriever.isRegIdStored(param_context);
        if (infostatus != 1) {
            GCMRegisterer objGCMReg = new GCMRegisterer();
            String regID = objGCMReg.registerAppWithGCMServer(param_context);
            if (!TextUtils.isEmpty(regID)) {
                RegistrationValidator objRegValidator = new RegistrationValidator();
                InfoRetriever objInfo = new InfoRetriever();
                StorageRegistration objStorage = new StorageRegistration();

                ExpHandler objExp = new ExpHandler();
                int L_valid_Status = objRegValidator.isValidRegId(regID);
                if (L_valid_Status != 1) {
                    objExp.displayException(3, param_context);
                    L_Status = -1;
                } else if (L_valid_Status == 1) {
                    objStorage.storeRegId(param_context, regID);
                    L_Status = 1;
                }
            }
        } else {
            L_Status = 1;
        }
        return L_Status;
    }

}
