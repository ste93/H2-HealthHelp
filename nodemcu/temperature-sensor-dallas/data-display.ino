/**
 * begins the u82g display, the display built-in in wifi kit 8
 */
void setupDisplay() {
	  u8g2.begin();
}

/**
 * write the text on the display
 * @param text a string containing the text to be written
 */
void drawTextCentered (const char *text) {
  u8g2.setFont(u8g2_font_logisoso32_tf); // set the target font to calculate the pixel width
  width = u8g2.getUTF8Width(text);    // calculate the pixel width of the text
  u8g2.setFontMode(0);    // enable transparent mode, which is faster
  u8g2.firstPage();
  do {
    u8g2_uint_t displayWidth = u8g2.getDisplayWidth();
    u8g2_uint_t offsetDraw = (displayWidth - width) / 2;
    u8g2.setFont(u8g2_font_logisoso32_tf);   // set the target font
    u8g2.drawUTF8(offsetDraw, 32, text);     // draw the scolling text
    u8g2.setFont(u8g2_font_logisoso32_tf);   // draw the current pixel width
    u8g2.setCursor(offsetDraw, 64);
    u8g2.print(text);          // this value must be lesser than 128 unless U8G2_16BIT is set
  } while ( u8g2.nextPage() );
}
