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

package com.cloudcontactai.twiliopolling.model

import com.cloudcontactai.twiliopolling.model.types.MessageStatus
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name="campaign_messages")
class CampaignMessage {
    @Id
    var id: Long? = null

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    var status: MessageStatus = MessageStatus.PENDING

    @Column(name = "sent_at")
    var sentAt : Instant? = null

    @Column(name ="twilio_id")
    var twilioId: String? = null
}
