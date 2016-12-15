# android-outline-textview
An implementation to support outlined (stroked) text in TextView and EditText widgets using canvas stroke

This implementation is inspired in [Nouman Hanif's](http://stackoverflow.com/users/1390871/nouman-hanif) answer to [this Stack Overflow question](http://stackoverflow.com/questions/3182393/android-textview-outline-text), making changes to support more features.

The actual features are:
 * Stroked text in TextView using the StrokedTextView widget, using a customizable color and width
 * Stroked text and text hints in EditText using the StrokedEditText widget, using differenciated customizable color and width for text and hint

## Installation

By the moment you can add this library as a git submodule to your project:

```bash
git submodule add git@github.com:biomorgoth/android-outline-textview.git
git submodule update --init
```

If requested, i could provide more installation methods.

## Usage

There are special attributes for each widget to set the desired color and width of the text stroke:

### **StrokedTextView** and **StrokedEditText**

 * **textStrokeColor**: sets the Color ResourceId for the stroke
 * **textStrokeWidth**: sets the width for the stroke, specified in Android's **sp** unit

### **StrokedEditText**

 * **textHintStrokeColor**: sets the Color ResourceId for the stroke when displaying hints
 * **textHintStrokeWidth**: sets the width for the stroke when displaying hints, specified in Android's **sp** unit

If the stroke width is not specified in any of these cases, the default width **0** is used, resulting in the same behavior as the base **TextView**/**EditText** widgets.

## Examples

```xml
<com.biomorgoth.outlinetextview.StrokedTextView
        android:id="@+id/your_text_view"
        android:text="I am a StrokedTextView!"
        android:textColor="@android:color/white"
        android:textSize="25sp"
        strokeAttrs:textStrokeColor="@android:color/black"
        strokeAttrs:textStrokeWidth="1.7"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
```

```xml
<com.biomorgoth.outlinetextview.StrokedEditText
        android:id="@+id/your_edit_text"
        android:hint="Write here!"
        android:textColor="@android:color/white"
        android:textColorHint="#5fff"
        android:textSize="25sp"
        strokeAttrs:textStrokeColor="@android:color/black"
        strokeAttrs:textStrokeWidth="1.7"
        strokeAttrs:textHintStrokeColor="#5000"
        strokeAttrs:textHintStrokeWidth="1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
```

## License

I chose Apache 2.0 License because... WTH, Android uses it i guess.

Copyright 2016 Evander Palacios

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
