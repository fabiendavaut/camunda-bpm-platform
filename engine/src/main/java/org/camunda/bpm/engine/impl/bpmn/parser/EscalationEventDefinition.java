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

package org.camunda.bpm.engine.impl.bpmn.parser;

import org.camunda.bpm.engine.impl.pvm.PvmActivity;

/**
 * Represents an escalation event definition that reference an 'escalation' element.
 *
 * @author Philipp Ossler
 *
 */
public class EscalationEventDefinition {

  protected final PvmActivity escalationHandler;
  protected final boolean cancelActivity;

  protected String escalationCode;

  public EscalationEventDefinition(PvmActivity escalationHandler, boolean cancelActivity, Escalation escalation) {
    this.escalationHandler = escalationHandler;
    this.cancelActivity = cancelActivity;
    this.escalationCode = escalation.getEscalationCode();
  }

  public EscalationEventDefinition(PvmActivity escalationHandler, boolean cancelActivity) {
    this.escalationHandler = escalationHandler;
    this.cancelActivity = cancelActivity;
  }

  public String getEscalationCode() {
    return escalationCode;
  }

  public PvmActivity getEscalationHandler() {
    return escalationHandler;
  }

  public boolean isCancelActivity() {
    return cancelActivity;
  }

  public void setEscalationCode(String escalationCode) {
    this.escalationCode = escalationCode;
  }
}