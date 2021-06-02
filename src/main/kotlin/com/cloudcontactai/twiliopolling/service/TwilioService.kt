/* Copyright 2021 Cloud Contact AI, Inc. All Rights Reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.cloudcontactai.twiliopolling.service

import com.cloudcontactai.twiliopolling.model.CampaignMessage
import com.cloudcontactai.twiliopolling.model.message.UpdateSmsStatusMessage
import com.cloudcontactai.twiliopolling.util.TwilioMessageStatus
import com.fasterxml.jackson.databind.ObjectMapper
import com.twilio.rest.api.v2010.account.Message
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.DependsOn
import org.springframework.jms.core.JmsTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
@DependsOn("twilioConfig")
class TwilioService {
    @Autowired
    lateinit var jmsTemplate: JmsTemplate

    @Value("\${cloudcontactai.queue.updateSmsStatus}")
    lateinit var updateSmsStatusQueue: String

    companion object{
        private val logger = LoggerFactory.getLogger(TwilioService::class.java)
    }

    @Async("taskExecutor")
    fun checkTwilioStatus(message: CampaignMessage) {
        try {
            val twilioMsg: Message = Message.fetcher(message.twilioId!!).fetch()
            val status = twilioMsg.status.name.toLowerCase()
            if (TwilioMessageStatus.FINALS.contains(status)) {
                logger.debug("Processing twilio status: $status")
                val updateRequest = UpdateSmsStatusMessage(
                    message.id,
                    message.twilioId,
                    status,
                    twilioMsg.errorCode?.toString(),
                    twilioMsg.errorMessage
                )
                jmsTemplate.convertAndSend(updateSmsStatusQueue, ObjectMapper().writeValueAsString(updateRequest))
            } else {
                logger.info("Ignoring twilio status: $status")
            }
        } catch (e: Exception) {
            logger.error("Error fetching twilio message: ${message.twilioId}: ${e.message}")
        }
    }
}