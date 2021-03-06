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
package org.camunda.bpm.engine.rest;

import org.camunda.bpm.engine.management.JobDefinitionQuery;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.management.JobDefinitionDto;
import org.camunda.bpm.engine.rest.dto.management.JobDefinitionQueryDto;
import org.camunda.bpm.engine.rest.dto.management.JobDefinitionSuspensionStateDto;
import org.camunda.bpm.engine.rest.sub.management.JobDefinitionResource;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * @author roman.smirnov
 */
@Produces(MediaType.APPLICATION_JSON)
public interface JobDefinitionRestService {

  public static final String PATH = "/job-definition";

  @Path("/{id}")
  JobDefinitionResource getJobDefinition(@PathParam("id") String jobDefinitionId);

  /**
   * Exposes the {@link JobDefinitionQuery} interface as a REST service.
   * @param uriInfo
   * @param firstResult
   * @param maxResults
   * @return
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<JobDefinitionDto> getJobDefinitions(@Context UriInfo uriInfo,
      @QueryParam("firstResult") Integer firstResult, @QueryParam("maxResults") Integer maxResults);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  List<JobDefinitionDto> queryJobDefinitions(JobDefinitionQueryDto queryDto,
      @QueryParam("firstResult") Integer firstResult,
      @QueryParam("maxResults") Integer maxResults);

  @GET
  @Path("/count")
  @Produces(MediaType.APPLICATION_JSON)
  CountResultDto getJobDefinitionsCount(@Context UriInfo uriInfo);

  @POST
  @Path("/count")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  CountResultDto queryJobDefinitionsCount(JobDefinitionQueryDto queryDto);

  @PUT
  @Path("/suspended")
  @Consumes(MediaType.APPLICATION_JSON)
  void updateSuspensionState(JobDefinitionSuspensionStateDto dto);

}
