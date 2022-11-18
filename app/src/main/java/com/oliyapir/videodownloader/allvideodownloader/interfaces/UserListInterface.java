package com.oliyapir.videodownloader.allvideodownloader.interfaces;

import com.oliyapir.videodownloader.allvideodownloader.model.FBStoryModel.NodeModel;
import com.oliyapir.videodownloader.allvideodownloader.model.story.TrayModel;

public interface UserListInterface {
    void userListClick(int position, TrayModel trayModel);
    void fbUserListClick(int position, NodeModel trayModel);
}
