
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

package com.cloudcontactai.twiliopolling.config

import com.twilio.Twilio
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TwilioConfig {

    @Value("\${twilio.accountSid}")
    lateinit var accountSid: String

    @Value("\${twilio.authToken}")
    lateinit var authToken: String

    @PostConstruct
    fun init(){
        Twilio.init(accountSid, authToken)
    }
}
