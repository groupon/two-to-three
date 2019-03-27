/**
 * Copyright 2018 Groupon.com
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
package com.groupon.openapi.twoothree;

/**
 * Enum with types of output, OpeanAPI 3 or swagger/openapi 2.0
 *
 * @author Alex Campelo (acampelo at groupon dot com)
 * @since 1.0.0
 */
public enum  OpenAPIFormat {
    /**
     * swagger or open api 2.0 format
     */
    openapi2,
    /**
     * openapi 3.0 format
     */
    openapi3
}
