#!/bin/bash

# Name of the emulator to start
EMULATOR_NAME=${1:-"Pixel_5_API_30"}  # Default to "Pixel_5_API_33" if no argument is passed

# Path to the Android SDK emulator tool
ANDROID_SDK_PATH="$HOME/Android/Sdk"  # Adjust if your SDK path is different
EMULATOR="$ANDROID_SDK_PATH/emulator/emulator"
ADB="$ANDROID_SDK_PATH/platform-tools/adb"

# Function to disable animations
disable_animations() {
    echo "Disabling animations on the emulator..."
    $ADB shell settings put global window_animation_scale 0
    $ADB shell settings put global transition_animation_scale 0
    $ADB shell settings put global animator_duration_scale 0
    echo "Animations disabled."
}

# Start the emulator
echo "Starting emulator: $EMULATOR_NAME..."
$EMULATOR -avd "$EMULATOR_NAME" -no-snapshot-load -no-boot-anim &

# Wait for the emulator to fully boot
echo "Waiting for emulator to boot..."
$ADB wait-for-device

# Disable animations
disable_animations

echo "Emulator is ready with animations disabled."
