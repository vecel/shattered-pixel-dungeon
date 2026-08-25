package com.shatteredpixel.shatteredpixeldungeon.utils;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;

public class GdxApplicationExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        if (Gdx.app != null) return;

        Preferences mockPrefs = mock(Preferences.class);
        when(mockPrefs.getString(anyString(), anyString())).thenReturn("en");
        when(mockPrefs.getString(anyString())).thenReturn("en");

        Files mockFiles = mock(Files.class);

        when(mockFiles.internal(anyString())).thenAnswer(invocation -> {
            String requestedFile = invocation.getArgument(0);
            String assetsPath = "./src/main/assets/";
            File physicalFile = new File(assetsPath + requestedFile);
            return new FileHandle(physicalFile);
        });


        Application mockApp = mock(Application.class);
        when(mockApp.getPreferences(anyString())).thenReturn(mockPrefs);

        Gdx.app = mockApp;
        Gdx.files = mockFiles;
    }
}