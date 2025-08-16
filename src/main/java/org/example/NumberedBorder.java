package org.example;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.border.AbstractBorder;

class NumberedBorder extends AbstractBorder {
	private static final long serialVersionUID = -5089118025935944759L;
	private final Color myColor;
	private final int padding = 5;

	NumberedBorder() {
		this.myColor = new Color(164, 164, 164);
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		if (!(c instanceof JTextArea)) return;

		JTextArea textArea = (JTextArea) c;
		Font font = textArea.getFont();
		FontMetrics metrics = g.getFontMetrics(font);
		int lineHeight = metrics.getHeight();
		int visibleLines = height / lineHeight;

		Color oldColor = g.getColor();
		g.setColor(myColor);

		// Calcula a largura necessária baseada no número máximo de dígitos
		int maxDigits = String.valueOf(visibleLines).length();
		int lineLeft = (maxDigits * metrics.stringWidth("0")) + (2 * padding);

		// Desenha os números das linhas
		for (int i = 0; i < visibleLines; i++) {
			String lineNumber = String.valueOf(i + 1);
			int py = (i * lineHeight) + metrics.getAscent();
			int px = lineLeft - metrics.stringWidth(lineNumber) - padding;
			g.drawString(lineNumber, px, py);
		}

		// Desenha a linha vertical
		g.drawLine(lineLeft + padding, 0, lineLeft + padding, height);
		g.setColor(oldColor);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		if (!(c instanceof JTextArea)) return new Insets(1, 1, 1, 1);

		JTextArea textArea = (JTextArea) c;
		FontMetrics metrics = textArea.getFontMetrics(textArea.getFont());
		int maxDigits = String.valueOf(textArea.getLineCount()).length();
		int left = (maxDigits * metrics.stringWidth("0")) + (3 * padding);

		return new Insets(1, left, 1, 1);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}
}