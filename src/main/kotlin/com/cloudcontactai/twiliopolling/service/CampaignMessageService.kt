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

import com.cloudcontactai.twiliopolling.model.types.MessageStatus
import com.cloudcontactai.twiliopolling.repository.CampaignMessageRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.temporal.ChronoUnit

@Repository
class CampaignMessageService {
    @Autowired
    lateinit var repository: CampaignMessageRepository

    @Autowired
    lateinit var twilioService: TwilioService

    @Value("\${cloudcontactai.messages-min-age}")
    var minAge: Long = 0L

    companion object{
        private val logger = LoggerFactory.getLogger(CampaignMessageService::class.java)
    }

    fun findPendingMessage() {
        // Only look for pending message since XX minutes to avoid colliding with statusCallback
        val since = Instant.now().minus(minAge, ChronoUnit.MINUTES)
        logger.debug("Looking for pending messages before $since")
        val pending = repository.findByStatusAndSentAtBefore(MessageStatus.SENDING, since)
        logger.info("${pending.size} pending messages found before $since")
        pending.forEach { twilioService.checkTwilioStatus(it) }
    }
}