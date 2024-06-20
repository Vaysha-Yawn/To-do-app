-dontusemixedcaseclassnames
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses
-renamesourcefileattribute SourceFile

-optimizations !method/repackage/variable
-optimizations !code/simplification/variable,!code/simplification/arithmetic,!code/simplification/cast,!code/simplification/member,!code/simplification/string,!field/*,!class/merging/*,!method/marking/private,!class/merging/*
-optimizationpasses 5

-allowaccessmodification
-dontoptimize
-dontpreverify

-useuniqueclassmembernames