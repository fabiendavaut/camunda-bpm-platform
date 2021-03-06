/*
 * Copyright © 2013-2018 camunda services GmbH and various authors (info@camunda.com)
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
package org.camunda.bpm.engine.test.jobexecutor;

import static org.camunda.bpm.engine.test.util.ClockTestUtil.incrementClock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.camunda.bpm.engine.impl.persistence.entity.JobEntity;
import org.camunda.bpm.engine.impl.persistence.entity.MessageEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Before;
import org.junit.Test;

public class JobExecutorAcquireJobsByTypeTest extends AbstractJobExecutorAcquireJobsTest {

  @Before
  public void prepareProcessEngineConfiguration() {
    configuration.setJobExecutorPreferTimerJobs(true);
  }

  @Test
  public void testProcessEngineConfiguration() {
    assertTrue(configuration.isJobExecutorPreferTimerJobs());
    assertFalse(configuration.isJobExecutorAcquireByDueDate());
    assertFalse(configuration.isJobExecutorAcquireByPriority());
  }

  @Test
  @Deployment(resources = "org/camunda/bpm/engine/test/jobexecutor/simpleAsyncProcess.bpmn20.xml")
  public void testMessageJobHasNoDueDateSet() {
    runtimeService.startProcessInstanceByKey("simpleAsyncProcess");

    Job job = managementService.createJobQuery().singleResult();
    assertNull(job.getDuedate());
  }

  @Test
  @Deployment(resources = {
    "org/camunda/bpm/engine/test/jobexecutor/simpleAsyncProcess.bpmn20.xml",
    "org/camunda/bpm/engine/test/jobexecutor/processWithTimerCatch.bpmn20.xml"
  })
  public void testTimerJobsArePreferred() {
    // first start process with timer job
    runtimeService.startProcessInstanceByKey("testProcess");
    // then start process with async task
    runtimeService.startProcessInstanceByKey("simpleAsyncProcess");
    // then start process with timer job
    runtimeService.startProcessInstanceByKey("testProcess");
    // and another process with async task
    runtimeService.startProcessInstanceByKey("simpleAsyncProcess");

    // increment clock so that timer events are acquirable
    incrementClock(70);

    List<JobEntity> acquirableJobs = findAcquirableJobs();
    assertEquals(4, acquirableJobs.size());
    assertTrue(acquirableJobs.get(0) instanceof TimerEntity);
    assertTrue(acquirableJobs.get(1) instanceof TimerEntity);
    assertTrue(acquirableJobs.get(2) instanceof MessageEntity);
    assertTrue(acquirableJobs.get(3) instanceof MessageEntity);
  }

}
