package com.lixh.jsSdk.base;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.lixh.jsSdk.jscrawler.AndroidEventEngine;
import com.lixh.jsSdk.jscrawler.JsCrawler;


/**
 * @author DeMon
 * @date 2018/8/8
 * @description
 */
public abstract class BaseAccessibilityService extends AccessibilityService {
    public AndroidEventEngine bridge;

    @Override
    public void onCreate() {
        super.onCreate();
        bridge = AndroidEventEngine.init(this);
        JsCrawler.getInstance().setEventEngine(bridge);
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.e("onAccessibilityEvent", accessibilityEvent.getEventType() + "---" + accessibilityEvent.getPackageName().toString() + "---" + accessibilityEvent.getClassName());
        switch (accessibilityEvent.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                onTypeViewClicked(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                onTypeViewLongClicked(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                onTypeViewFocused(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                onTypeViewSelected(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                onTypeViewTextChanged(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                onTypeWindowStateChanged(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                onTypeNotificationStateChanged(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                onTypeTouchExplorationGestureStart(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                onTypeTouchExplorationGestureEnd(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                onTypeViewHoverEnter(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                onTypeViewHoverExit(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                onTypeViewScrolled(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                onTypeViewTextSelectionChanged(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                onTypeWindowContentChanged(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                onTypeAnnouncement(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
                onTypeGestureDetectionStart(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
                onTypeGestureDetectionEnd(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
                onTypeTouchInteractionStart(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
                onTypeTouchInteractionEnd(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                onTypeViewAccessibilityFocused(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                onTypeWindowsChanged(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
                onTypeViewAccessibilityFocusCleared(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED:
                onTypeViewContextClicked(accessibilityEvent);
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
                onTypeViewTextTraversedAtMovementGranularity(accessibilityEvent);
                break;
        }

    }

    public void onTypeViewAccessibilityFocusCleared(AccessibilityEvent accessibilityEvent) {

    }

    public void onTypeViewAccessibilityFocused(AccessibilityEvent accessibilityEvent) {

    }

    public void onTypeViewClicked(AccessibilityEvent accessibilityEvent) {

    }

    public void onTypeViewLongClicked(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeViewSelected(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeViewFocused(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeViewTextChanged(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeWindowStateChanged(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeNotificationStateChanged(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeViewHoverEnter(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeViewHoverExit(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeTouchExplorationGestureStart(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeTouchExplorationGestureEnd(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeWindowContentChanged(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeViewScrolled(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeViewTextSelectionChanged(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeAnnouncement(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeViewTextTraversedAtMovementGranularity(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeGestureDetectionStart(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeGestureDetectionEnd(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeTouchInteractionStart(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeTouchInteractionEnd(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeWindowsChanged(AccessibilityEvent accessibilityEvent) {
    }

    public void onTypeViewContextClicked(AccessibilityEvent accessibilityEvent) {
    }

    @Override
    public void onInterrupt() {
    }

}

