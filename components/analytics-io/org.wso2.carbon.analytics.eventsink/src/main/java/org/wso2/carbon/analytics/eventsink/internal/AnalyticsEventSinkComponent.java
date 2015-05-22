/*
*  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.analytics.eventsink.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.analytics.dataservice.AnalyticsDataService;
import org.wso2.carbon.analytics.eventsink.AnalyticsEventSinkService;
import org.wso2.carbon.analytics.eventsink.AnalyticsEventSinkServiceImpl;
import org.wso2.carbon.analytics.eventsink.internal.util.ServiceHolder;
import org.wso2.carbon.analytics.eventsink.subscriber.AnalyticsEventStreamListener;
import org.wso2.carbon.core.ServerStartupObserver;
import org.wso2.carbon.databridge.core.definitionstore.AbstractStreamDefinitionStore;
import org.wso2.carbon.event.stream.core.EventStreamListener;
import org.wso2.carbon.event.stream.core.EventStreamService;
import org.wso2.carbon.utils.Axis2ConfigurationContextObserver;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

/**
 * @scr.component name="analytics.eventsink.comp" immediate="true"
 * @scr.reference name="registry.streamdefn.comp"
 * interface="org.wso2.carbon.databridge.core.definitionstore.AbstractStreamDefinitionStore"
 * cardinality="1..1" policy="dynamic" bind="setStreamDefinitionStoreService" unbind="unsetStreamDefinitionStoreService"
 * @scr.reference name="event.stream.service" interface="org.wso2.carbon.event.stream.core.EventStreamService"
 * cardinality="1..1" policy="dynamic" bind="setEventStreamService" unbind="unsetEventStreamService"
 * @scr.reference name="analytics.component" interface="org.wso2.carbon.analytics.dataservice.AnalyticsDataService"
 * cardinality="1..1" policy="dynamic" bind="setAnalyticsDataservice" unbind="unsetAnalyticsDataservice"
 */

public class AnalyticsEventSinkComponent {
    private static Log log = LogFactory.getLog(AnalyticsEventSinkComponent.class);

    protected void activate(ComponentContext componentContext) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Started the Analytics Event Sink component");
            }
            ServiceHolder.setAnalyticsEventSinkService(new AnalyticsEventSinkServiceImpl());
            componentContext.getBundleContext().registerService(EventStreamListener.class.getName(),
                    ServiceHolder.getAnalyticsEventStreamListener(), null);
            componentContext.getBundleContext().registerService(AnalyticsEventSinkService.class.getName(),
                    ServiceHolder.getAnalyticsEventSinkService(), null);
            componentContext.getBundleContext().registerService(ServerStartupObserver.class.getName(),
                    AnalyticsEventSinkServerStartupObserver.getInstance(), null);
            ServiceHolder.setAnalyticsDSConnector(new AnalyticsDSConnector());
        } catch (Throwable e) {
            log.error("Error while activating the AnalyticsEventSinkComponent.", e);
        }
    }

    protected void deactivate(ComponentContext componentContext) {
        if (log.isDebugEnabled()) {
            log.debug("Stopped AnalyticsEventSink component");
        }
    }

    protected void setStreamDefinitionStoreService(AbstractStreamDefinitionStore abstractStreamDefinitionStore) {
        ServiceHolder.setStreamDefinitionStoreService(abstractStreamDefinitionStore);
    }

    protected void unsetStreamDefinitionStoreService(AbstractStreamDefinitionStore abstractStreamDefinitionStore) {
        ServiceHolder.setStreamDefinitionStoreService(null);
    }

    protected void setEventStreamService(EventStreamService eventStreamService) {
        ServiceHolder.setAnalyticsEventStreamListener(new AnalyticsEventStreamListener());
        ServiceHolder.setEventStreamService(eventStreamService);
    }

    protected void unsetEventStreamService(EventStreamService eventStreamService) {
        ServiceHolder.setEventStreamService(null);
    }

    protected void setAnalyticsDataservice(AnalyticsDataService analyticsDataservice) {
        ServiceHolder.setAnalyticsDataService(analyticsDataservice);
    }

    protected void unsetAnalyticsDataservice(AnalyticsDataService analyticsDataservice) {
        ServiceHolder.setAnalyticsDataService(null);
    }
}