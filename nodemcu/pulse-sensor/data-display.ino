#include <U8g2lib.h>

//U8g2 Contructor
U8G2_SSD1306_128X32_UNIVISION_F_SW_I2C u8g2(U8G2_R0, /* clock=*/ 5, /* data=*/ 4, /* reset=*/ 16);
u8g2_uint_t offset;     // current offset for the scrolling text
u8g2_uint_t width;      // pixel width of the scrolling text (must be lesser than 128 unless U8G2_16BIT is defined

void setupDisplay() {
	  u8g2.begin();
}

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
