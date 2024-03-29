/*
 * Copyright (C) 2010 ZXing authors
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

package com.example.testcppdemo;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 检测并捕捉画面 预览图像帧
 */
final class PreviewCallback implements Camera.PreviewCallback {

  private static final String TAG = PreviewCallback.class.getSimpleName();

  private final CameraConfigurationManager configManager;
  private final boolean useOneShotPreviewCallback;
  private Handler previewHandler;
  private int previewMessage;

  PreviewCallback(CameraConfigurationManager configManager, boolean useOneShotPreviewCallback) {
    this.configManager = configManager;
    this.useOneShotPreviewCallback = useOneShotPreviewCallback;
  }

  void setHandler(Handler previewHandler, int previewMessage) {
    this.previewHandler = previewHandler;
    this.previewMessage = previewMessage;
  }

  /**
   * 接口方法 当preview界面展示出来的时候向DecodeHandler发送一个decode消息，DecodeHandler收到该消息后会执行decode方法来解码
   */
  public void onPreviewFrame(byte[] data, Camera camera) {
    Log.i(TAG, "onPreviewFrame: ");
    Point cameraResolution = configManager.getCameraResolution();
    if (!useOneShotPreviewCallback) {
      camera.setPreviewCallback(null);
    }
    if (previewHandler != null) {
      Message message = previewHandler.obtainMessage(previewMessage, cameraResolution.x,
          cameraResolution.y, data);
      message.sendToTarget();
      Log.i(TAG, "onPreviewFrame: data "+data);
      previewHandler = null;
    } else {
      Log.d(TAG, "Got preview callback, but no handler for it");
    }
  }

}
