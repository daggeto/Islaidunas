/*
 * Copyright 2013 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.islaidunas.core.module;

import android.content.Context;
import android.util.Log;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.islaidunas.core.dbxStoreOrm.EntityManager;
import com.islaidunas.core.ui.ActionBarOwner;
import com.islaidunas.core.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = MainActivity.class, library = true, complete = false)
public class MainActivityModule {
    @Provides @Singleton ActionBarOwner provideActionBarOwner() {
        return new ActionBarOwner();
    }

    @Provides @Singleton DbxAccountManager provideDbxAccountManager(Context context){
        return DbxAccountManager.getInstance(context, "4y1y08z6k86rhgo", "h40y2jfc4fr3bmk");
    }

    @Provides DbxAccount provideDbxAccount(DbxAccountManager manager){
        return manager.getLinkedAccount();
    }

    @Provides DbxDatastore provideDbxDatastore(DbxAccount account){
        try {
            return DbxDatastore.openDefault(account);
        } catch (DbxException e) {
            Log.e("DbxException", e.toString());
            return null;
        }
    }

    @Provides EntityManager provideEntityManager(DbxDatastore store){
        return new EntityManager(store);
    }
}
