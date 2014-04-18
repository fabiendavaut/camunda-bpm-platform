/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.engine.impl.persistence.entity;

import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.persistence.AbstractManager;


/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class PropertyManager extends AbstractManager {

  public PropertyEntity findPropertyById(String propertyId) {
    return getDbSqlSession().selectById(PropertyEntity.class, propertyId);
  }

  public void acquireExclusiveLock() {
    // do not perform locking if H2 database is used. H2 uses table level locks
    // by default which may cause deadlocks if the deploy command needs to get a new
    // Id using the DbIdGenerator while performing a deployment.
    if(!"h2".equals(Context.getCommandContext().getDbSqlSession().getDbSqlSessionFactory().getDatabaseType())) {

      // We lock a special deployment lock property
      getDbSqlSession().lock("lockDeploymentLockProperty");
    }

  }

}
