/*
 * Copyright (c) 2014-2019 Aurélien Broszniowski
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

package io.rainfall.store.record.tc;

import io.rainfall.store.record.Store;
import io.rainfall.store.record.StoreTest;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.terracottatech.store.StoreException;
import com.terracottatech.store.configuration.DatasetConfiguration;
import com.terracottatech.store.configuration.MemoryUnit;
import com.terracottatech.store.manager.DatasetManager;

import static com.terracottatech.store.manager.DatasetManager.embedded;

public class TcStoreTest extends StoreTest {

  @Rule
  public TestName name = new TestName();

  @Override
  protected Store createStore() throws StoreException {
    String resourceName = getClass().getSimpleName() + "." + name.getMethodName();
    DatasetManager datasetManager = embedded()
        .offheap(resourceName, 4, MemoryUnit.MB)
        .build();
    DatasetConfiguration config = datasetManager.datasetConfiguration()
        .offheap(resourceName)
        .build();
    return new RainfallStore(datasetManager, config);
  }
}
