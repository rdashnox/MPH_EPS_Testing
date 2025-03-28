/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import javax.swing.text.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 *
 * @author dashcodes
 */

public class RestrictedInput extends PlainDocument {
     private boolean allowLetters;
    private boolean allowSpaces;
    private boolean allowHyphens;
    private boolean allowApostrophes;
    private boolean allowNumbers;
    private int maxLength;

    // Constructor with comprehensive input restrictions
    public RestrictedInput() {
        this(true, true, false, false, false, 50); // Default: letters and spaces, max 50 chars
    }

    public RestrictedInput(boolean allowLetters, boolean allowSpaces, 
                                   boolean allowHyphens, boolean allowApostrophes, 
                                   boolean allowNumbers,
                                   int maxLength) {
        this.allowLetters = allowLetters;
        this.allowSpaces = allowSpaces;
        this.allowHyphens = allowHyphens;
        this.allowApostrophes = allowApostrophes;
        this.allowNumbers = allowNumbers;
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }

        // Check total length after insertion
        int currentLength = getLength();
        if (currentLength + str.length() > maxLength) {
            return; // Prevent exceeding max length
        }

        // Create a StringBuilder to filter the input
        StringBuilder filteredStr = new StringBuilder();
        
        // Iterate through each character in the input string
        for (char c : str.toCharArray()) {
            if (isValidCharacter(c)) {
                filteredStr.append(c);
            }
        }
        
        // If there are valid characters after filtering, insert them
        if (filteredStr.length() > 0) {
            super.insertString(offs, filteredStr.toString(), a);
        }
    }
    
    @Override
    public void replace(int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
        if (str == null) {
            return;
        }

        // Check total length after replacement
        int currentLength = getLength();
        if (currentLength - length + str.length() > maxLength) {
            return; // Prevent exceeding max length
        }

        StringBuilder filteredStr = new StringBuilder();
        
        for (char c : str.toCharArray()) {
            if (isValidCharacter(c)) {
                filteredStr.append(c);
            }
        }
        
        if (filteredStr.length() > 0) {
            super.replace(offset, length, filteredStr.toString(), attrs);
        }
    }

    // Helper method to validate characters based on input restrictions
    private boolean isValidCharacter(char c) {
        return (allowLetters && Character.isLetter(c)) ||
               (allowSpaces && c == ' ') ||
               (allowHyphens && c == '-') ||
               (allowApostrophes && c == '\'') ||
               (allowNumbers && Character.isDigit(c));
    }

    // Static method to create a numbers-only document
    public static RestrictedInput numbersOnly(int maxLength) {
        return new RestrictedInput(
            false,   // No letters
            false,   // No spaces
            false,   // No hyphens
            false,   // No apostrophes
            true,    // Allow numbers
            maxLength
        );
    }

    // Getter and setter methods for input restrictions
    public void setAllowLetters(boolean allowLetters) {
        this.allowLetters = allowLetters;
    }

    public void setAllowSpaces(boolean allowSpaces) {
        this.allowSpaces = allowSpaces;
    }

    public void setAllowHyphens(boolean allowHyphens) {
        this.allowHyphens = allowHyphens;
    }

    public void setAllowApostrophes(boolean allowApostrophes) {
        this.allowApostrophes = allowApostrophes;
    }

    public void setAllowNumbers(boolean allowNumbers) {
        this.allowNumbers = allowNumbers;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
