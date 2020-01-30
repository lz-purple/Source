/*
 * Copyright (C) 2017 The Android Open Source Project
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
package android.appsecurity.cts;

import com.android.compatibility.common.tradefed.build.CompatibilityBuildHelper;
import com.android.tradefed.build.IBuildInfo;
import com.android.tradefed.device.CollectingOutputReceiver;
import com.android.tradefed.testtype.DeviceTestCase;
import com.android.tradefed.testtype.IBuildReceiver;

public class OverlayHostTest extends DeviceTestCase implements IBuildReceiver {
    private static final String PKG = "com.android.cts.overlayapp";
    private static final String APK = "CtsOverlayApp.apk";
    private CompatibilityBuildHelper mBuildHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getDevice().uninstallPackage(PKG);
    }

    @Override
    protected void tearDown() throws Exception {
        getDevice().uninstallPackage(PKG);
        super.tearDown();
    }

    @Override
    public void setBuild(IBuildInfo buildInfo) {
        mBuildHelper = new CompatibilityBuildHelper(buildInfo);
    }

    public void testInstallingOverlayHasNoEffect() throws Exception {
        assertNull(getDevice().installPackage(mBuildHelper.getTestFile(APK), false, false));
        CollectingOutputReceiver receiver = new CollectingOutputReceiver();
        getDevice().getIDevice().executeShellCommand("cmd overlay list", receiver);

        // The package of the installed overlay should not appear in the overlay manager list.
        final String output = receiver.getOutput();
        assertFalse(output.contains(PKG));
    }

}
