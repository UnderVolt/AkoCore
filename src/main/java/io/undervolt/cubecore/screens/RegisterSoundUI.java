package io.undervolt.cubecore.screens;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.lwjgl.input.Keyboard;

import io.undervolt.cubecore.CubecoreMod;
import io.undervolt.cubecore.net.CustomPayloadPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class RegisterSoundUI extends GuiScreen {

    private GuiTextField urlField;
    private GuiTextField nameField;
    private GuiTextField currentFocus;

    @Override
    public void initGui() {
        this.nameField = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, this.height / 2 - 56, 200, 20);
        this.nameField.setFocused(true);
        this.currentFocus = this.nameField;

        this.urlField = new GuiTextField(1, this.fontRenderer, this.width / 2 - 100, this.height / 2 - 31, 200, 20);
        this.urlField.setMaxStringLength(Integer.MAX_VALUE);

        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + 25, 200, 20, "Download"));
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.nameField.textboxKeyTyped(typedChar, keyCode);
        this.urlField.textboxKeyTyped(typedChar, keyCode);

        super.keyTyped(typedChar, keyCode);

        if(keyCode == Keyboard.KEY_RETURN) this.emitResponse();

        if (keyCode == Keyboard.KEY_TAB) {
            if (this.currentFocus == this.nameField) {
                this.nameField.setFocused(false);
                this.urlField.setFocused(true);
                this.currentFocus = this.urlField;
            } else if (currentFocus == this.urlField) {
                this.urlField.setFocused(false);
                this.nameField.setFocused(true);
                this.currentFocus = this.nameField;
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.urlField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 2) this.emitResponse();
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if(this.nameField != null && this.urlField != null) {
            this.nameField.drawTextBox();
            this.urlField.drawTextBox();
        }

        String name = "Song name:";

        this.fontRenderer.drawString(name,   this.width / 2 - 100 - ( 5 + this.fontRenderer.getStringWidth(name) ), this.height / 2 - 50, 0xFFFFFF);
        this.fontRenderer.drawString("URL:", this.width / 2 - 100 - ( 5 + this.fontRenderer.getStringWidth("URL:") ), this.height / 2 - 25, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        if(this.nameField != null && this.urlField != null) {
            this.nameField.updateCursorCounter();
            this.urlField.updateCursorCounter();
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void emitResponse(){
        if(this.nameField.getText().isEmpty() || this.urlField.getText().isEmpty()) return;

        JsonObject json = new JsonObject();
        json.addProperty("task", "rsound:add");
        json.addProperty("name", this.nameField.getText());
        try {
            json.addProperty("url", URLEncoder.encode(this.urlField.getText(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {}

        // CubecoreMod.INSTANCE.network.sendToServer(new CustomPayloadPacket(new Gson().toJson(json)));
        Minecraft.getMinecraft().displayGuiScreen(null);
    }
}