/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package org.gradle.play.integtest.fixtures

import org.gradle.testing.internal.util.Specification
import org.gradle.util.CollectionUtils

class RunningPlayAppTest extends Specification {
    def "can parse HTTP port from output"() {
        expect:
        RunningPlayApp.regexParseHttpPort(output, occurrence) == result

        where:
        occurrence | result                    | output
        0          | 57696                     | playOutput(":runPlayBinary", "57696")
        1          | 57696                     | CollectionUtils.join('stuff\n', playOutput(":runPlayBinary", "12345"), playOutput(":runPlayBinary", "57696"))
        0          | RunningPlayApp.UNASSIGNED | "no port"
    }

    def playOutput(path, port) {
        "Running Play App ($path) at http://localhost:$port/"
    }
}
