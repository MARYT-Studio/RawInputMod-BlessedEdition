# Raw Input Mod - Blessed Edition

## Overview

Use raw input on Minecraft 1.12.2. Make mouse input feel smoother and fix mouse jitter.

Besides, if you have encountered some issue like mouse cannot work properly in some GUIs (Astral Sorcery's Observatory GUI for example) after installed RawInput, you may switch to this fork to solve the problem without having to delete this mod.

If you still find it works not properly, please report it to me directly.

## Features

### Auto Capture & Manual Control

With this fork installed, Minecraft capture your **raw** mouse input automatically, while these manual control commands are still kept. You will need them only when certain situations, like pluging in-and-out your mouse physically.

`/rawinput` - Toggles the raw input.

`/rescan` - Rescans for a mouse, Use if mouse input is not working.

Both commands have keybindings that are unbound by default.

### \*Pause\* Raw Input Mode in Specific GUIs (since v1.7.0)

This fork has a config file `rawinput.cfg`. With this config, you can set in which GUIs RawInput will disable itself automatically. Then, after you exit from those GUIs, RawInput will come back instantly. No need to toggle it manually.

Like you guessed, Astral Sorcery's Observatory GUI is added to the config by default.

### Operating System Checking (since v1.8.0)

Raw Input mode _may be only needed_ by Windows devices.

If your computers are running other OS, by default, this fork will detect this and disable its entire functionality to prevent breaking your mouse input.

If you are sure that you need it on your device, you can force this mod to be enabled via config.  

## Credit

This project is a fork of [seanld03/RawInputMod-1.12.2-gui-fix](https://github.com/seanld03/RawInputMod-1.12.2-gui-fix), [Fluorides/RawInputMod1.12.2](https://github.com/Fluorides/RawInputMod1.12.2), [xCuri0/RawInputMod](https://github.com/xCuri0/RawInputMod).

This fork now utilizes [CleanroomMC/TemplateDevEnv](https://github.com/CleanroomMC/TemplateDevEnv) as development environment. 

Logo image is created by DALL-E of OpenAI.
