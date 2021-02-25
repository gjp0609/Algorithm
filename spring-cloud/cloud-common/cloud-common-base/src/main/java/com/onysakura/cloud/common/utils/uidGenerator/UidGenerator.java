/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
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
package com.onysakura.cloud.common.utils.uidGenerator;


import com.onysakura.cloud.common.utils.uidGenerator.exception.UidGenerateException;
import com.onysakura.cloud.common.utils.uidGenerator.impl.DefaultUidGenerator;

/**
 * Represents a unique id generator.
 *
 * @author yutianbao
 */
public interface UidGenerator {

    /**
     * Get a unique ID
     *
     * @return UID
     */
    long getUID() throws UidGenerateException;

    /**
     * Parse the UID into elements which are used to generate the UID. <br>
     * Such as timestamp & workerId & sequence...
     *
     * @return Parsed info
     */
    String parseUID(long uid);

    /**
     * 最大支持 4096 个实例
     *
     * @param workerId 0 ~ 4095
     */
    static UidGenerator getUidGenerator(Long workerId) {
        return new DefaultUidGenerator(workerId);
    }

    /**
     * 最大支持 4096 个实例，共 12 位，取前 5 位作机器 id，后 7 位作业务id
     *
     * @param machineId  0 ~ 31
     * @param businessId 0 ~ 127
     */
    static UidGenerator getUidGenerator(Long machineId, Long businessId) {
        long workerId = (machineId << 7) + businessId;
        return new DefaultUidGenerator(workerId);
    }
}
