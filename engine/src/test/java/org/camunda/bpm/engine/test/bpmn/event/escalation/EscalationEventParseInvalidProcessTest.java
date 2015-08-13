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

package org.camunda.bpm.engine.test.bpmn.event.escalation;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import junit.framework.AssertionFailedError;

/**
 * Parse an invalid process definition and assert the error message.
 *
 * @author Philipp Ossler
 */
@RunWith(Parameterized.class)
public class EscalationEventParseInvalidProcessTest {

  private static final String PROCESS_DEFINITION_DIRECTORY = "org/camunda/bpm/engine/test/bpmn/event/escalation/";

  @Parameters(name = "{index}: process definition = {0}, expected error message = {1}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        { "EscalationEventParseInvalidProcessTest.missingIdOnEscalation.bpmn20.xml", "escalation must have an id" },
        { "EscalationEventParseInvalidProcessTest.invalidAttachement.bpmn20.xml", "An escalation boundary event should only be attached to a subprocess or a call activity" },
        { "EscalationEventParseInvalidProcessTest.invalidEscalationRefOnBoundaryEvent.bpmn20.xml", "could not find escalation with id 'invalid-escalation'" },
        { "EscalationEventParseInvalidProcessTest.multipleEscalationBoundaryEventsWithSameEscalationCode.bpmn20.xml", "multiple escalation boundary events with the same escalationCode 'escalationCode' are not supported on same scope" },
        { "EscalationEventParseInvalidProcessTest.multipleEscalationBoundaryEventsWithoutEscalationCode.bpmn20.xml", "The same scope can not contains an escalation boundary event without escalation code and another one with escalation code."},
        { "EscalationEventParseInvalidProcessTest.missingEscalationCodeOnIntermediateThrowingEscalationEvent.bpmn20.xml", "throwing escalation event must have an 'escalationCode'" },
        { "EscalationEventParseInvalidProcessTest.missingEscalationRefOnIntermediateThrowingEvent.bpmn20.xml", "escalationEventDefinition does not have required attribute 'escalationRef'" },
        { "EscalationEventParseInvalidProcessTest.invalidEscalationRefOnIntermediateThrowingEvent.bpmn20.xml", "could not find escalation with id 'invalid-escalation'" }
    });
  }

  @Parameter(0)
  public String processDefinitionResource;

  @Parameter(1)
  public String expectedErrorMessage;

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule(PluggableProcessEngineTestCase.getProcessEngine());

  protected RepositoryService repositoryService;

  @Before
  public void initServices() {
    repositoryService = rule.getRepositoryService();
  }

  @Test
  public void testParseInvalidProcessDefinition() {
    try {
      String deploymentId = repositoryService.createDeployment()
        .addClasspathResource(PROCESS_DEFINITION_DIRECTORY + processDefinitionResource)
        .deploy().getId();

      // in case that the deployment do not fail
      repositoryService.deleteDeployment(deploymentId, true);

      fail("exception expected: " + expectedErrorMessage);
    } catch (Exception e) {
      assertExceptionMessageContainsText(e, expectedErrorMessage);
    }
  }

  public void assertExceptionMessageContainsText(Exception e, String expectedMessage) {
    String actualMessage = e.getMessage();
    if (actualMessage == null || !actualMessage.contains(expectedMessage)) {
      throw new AssertionFailedError("expected presence of [" + expectedMessage + "], but was [" + actualMessage + "]");
    }
  }
}
