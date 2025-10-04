public class wildcards {
    // Implement brute-force pattern matching with wildcards and escaping
    public static void main(String[] args) {
        String T = "How had she been so wrong? All her instincts and intuition completely failed her";
        
        // Test cases for different wildcard and escaping scenarios
        String P1 = "How*wrong";      // Should match - * is wildcard
        String P2 = "wrong\\?";       // Should match "wrong?" - \? is literal ?
        String P3 = "How\\*wrong";    // Should NOT match - \* is literal *
        String P4 = "comp?etely";     // Should match "completely" - ? is wildcard
        String P5 = "wrong\\\\";      // Should NOT match - \\ is literal \
        String P6 = "How*?";          // Should match - * and ? are wildcards
        String P7 = "she*decisions";  // Extended text test
        
        System.out.println("Text: \"" + T + "\"");
        System.out.println();
        
        System.out.println("=== Brute Force Results ===");
        System.out.println("Pattern: \"" + P1 + "\" -> " + bruteForceSearch(T, P1));
        System.out.println("Pattern: \"" + P2 + "\" -> " + bruteForceSearch(T, P2));  
        System.out.println("Pattern: \"" + P3 + "\" -> " + bruteForceSearch(T, P3));
        System.out.println("Pattern: \"" + P4 + "\" -> " + bruteForceSearch(T, P4));
        System.out.println("Pattern: \"" + P5 + "\" -> " + bruteForceSearch(T, P5));
        System.out.println("Pattern: \"" + P6 + "\" -> " + bruteForceSearch(T, P6));
        
        System.out.println("\n=== Sunday Algorithm Results ===");
        System.out.println("Pattern: \"" + P1 + "\" -> " + sundaySearch(T, P1));
        System.out.println("Pattern: \"" + P2 + "\" -> " + sundaySearch(T, P2));  
        System.out.println("Pattern: \"" + P3 + "\" -> " + sundaySearch(T, P3));
        System.out.println("Pattern: \"" + P4 + "\" -> " + sundaySearch(T, P4));
        System.out.println("Pattern: \"" + P5 + "\" -> " + sundaySearch(T, P5));
        System.out.println("Pattern: \"" + P6 + "\" -> " + sundaySearch(T, P6));
        
        // Test with extended text
        String extendedText = T + " She had so heavily relied on both when making decisions";
        System.out.println("\nExtended text test: " + extendedText);
        System.out.println("Brute Force Pattern: \"" + P7 + "\" -> " + bruteForceSearch(extendedText, P7));
        System.out.println("Sunday Pattern: \"" + P7 + "\" -> " + sundaySearch(extendedText, P7));
        
        // Additional test cases for Sunday algorithm
        System.out.println("\n=== Additional Sunday Tests ===");
        String simpleText = "abcdefghijk";
        String[] simplePatterns = {"c?e", "a*k", "def", "x?z", "ab*ij"};
        
        for (String pattern : simplePatterns) {
            System.out.println("Text: \"" + simpleText + "\", Pattern: \"" + pattern + "\"");
            System.out.println("  Brute Force: " + bruteForceSearch(simpleText, pattern));
            System.out.println("  Sunday: " + sundaySearch(simpleText, pattern));
        }
        
        // Specific test cases for wildcard alignment logic
        System.out.println("\n=== Wildcard Alignment Tests ===");
        String testText = "xabcdefg";
        String[] wildcardPatterns = {
            "ab?d",     // 'b' at pos 1, '?' at pos 2 - should align with '?' when seeing 'c'
            "a?cd",     // 'a' at pos 0, '?' at pos 1 - should align with '?' when seeing 'b'  
            "?bcd",     // no 'a' in pattern, '?' at pos 0 - should align with '?'
            "abc?"      // 'c' at pos 2, '?' at pos 3 - should align with '?' when seeing 'd'
        };
        
        for (String pattern : wildcardPatterns) {
            System.out.println("Text: \"" + testText + "\", Pattern: \"" + pattern + "\"");
            System.out.println("  Brute Force: " + bruteForceSearch(testText, pattern));
            System.out.println("  Sunday: " + sundaySearch(testText, pattern));
        }
        
        // Test cases specifically for asterisk handling in Sunday algorithm
        System.out.println("\n=== Asterisk Handling Tests ===");
        String[] asteriskTexts = {
            "hello world test",
            "programming is fun", 
            "abc123def456"
        };
        String[] asteriskPatterns = {
            "hello*test",      // Should match
            "prog*fun",        // Should match  
            "*def*",           // Should match
            "hello*xyz",       // Should not match
            "abc*123*456"      // Should match
        };
        
        for (String text : asteriskTexts) {
            for (String pattern : asteriskPatterns) {
                boolean bruteResult = bruteForceSearch(text, pattern);
                boolean sundayResult = sundaySearch(text, pattern);
                System.out.println("Text: \"" + text + "\", Pattern: \"" + pattern + "\"");
                System.out.println("  Brute Force: " + bruteResult + ", Sunday: " + sundayResult + 
                    (bruteResult == sundayResult ? " ✓" : " ✗ MISMATCH"));
            }
        }
    }
    
    // Brute force with wildcards (unchanged)
    public static boolean bruteForceSearch(String text, String pattern) {
        int n = text.length();
        
        for (int i = 0; i < n; i++) {
            if (isMatch(text, i, pattern, 0)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Recursive helper method for brute force (unchanged)
    private static boolean isMatch(String text, int textIndex, String pattern, int patternIndex) {
        if (patternIndex >= pattern.length()) {
            return true;
        }
        
        if (textIndex >= text.length()) {
            while (patternIndex < pattern.length()) {
                if (patternIndex < pattern.length() - 1 && pattern.charAt(patternIndex) == '\\') {
                    if (isEscapingBackslash(pattern, patternIndex)) {
                        return false;
                    }
                    patternIndex++;
                }
                if (patternIndex < pattern.length() && pattern.charAt(patternIndex) != '*') {
                    return false;
                }
                patternIndex++;
            }
            return true;
        }
        
        if (patternIndex < pattern.length() - 1 && pattern.charAt(patternIndex) == '\\' && 
            isEscapingBackslash(pattern, patternIndex)) {
            char escapedChar = pattern.charAt(patternIndex + 1);
            if (text.charAt(textIndex) == escapedChar) {
                return isMatch(text, textIndex + 1, pattern, patternIndex + 2);
            } else {
                return false;
            }
        }
        
        char currentPatternChar = pattern.charAt(patternIndex);
        
        if (currentPatternChar == '*') {
            if (isMatch(text, textIndex, pattern, patternIndex + 1)) {
                return true;
            }
            
            for (int i = textIndex; i < text.length(); i++) {
                if (isMatch(text, i + 1, pattern, patternIndex + 1)) {
                    return true;
                }
            }
            return false;
        }
        
        if (currentPatternChar == '?') {
            return isMatch(text, textIndex + 1, pattern, patternIndex + 1);
        }
        
        if (text.charAt(textIndex) == currentPatternChar) {
            return isMatch(text, textIndex + 1, pattern, patternIndex + 1);
        }
        
        return false;
    }
    
    // Helper method to check if backslash escapes next character (unchanged)
    private static boolean isEscapingBackslash(String pattern, int index) {
        if (pattern.charAt(index) != '\\' || index >= pattern.length() - 1) {
            return false;
        }
        
        int backslashCount = 0;
        int pos = index;
        
        while (pos >= 0 && pattern.charAt(pos) == '\\') {
            backslashCount++;
            pos--;
        }
        
        return backslashCount % 2 == 1;
    }

    // Sunday algorithm with wildcards support
    public static boolean sundaySearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        
        if (m == 0) return true;
        if (n == 0) return m == 0;
        
        // Handle patterns with '*' using modified Sunday approach
        if (containsUnescapedAsterisk(pattern)) {
            return sundaySearchWithAsterisk(text, pattern);
        }
        
        // Preprocess - create shift table with wildcard logic
        int[] shift = new int[128]; // ASCII character set
        
        // Initialize all shifts to m + 1 (pattern length + 1)
        for (int i = 0; i < 128; i++) {
            shift[i] = m + 1;
        }
        
        // First pass: record all character positions (including wildcards)
        int[] lastOccurrence = new int[128];
        int lastWildcardPos = -1;
        
        // Initialize to -1 (not found)
        for (int i = 0; i < 128; i++) {
            lastOccurrence[i] = -1;
        }
        
        // Fill positions, handling escapes
        for (int i = 0; i < m; i++) {
            if (i < m - 1 && pattern.charAt(i) == '\\' && isEscapingBackslash(pattern, i)) {
                // This is an escaped character
                char escapedChar = pattern.charAt(i + 1);
                lastOccurrence[escapedChar] = i;
                i++; // Skip the escaped character
            } else {
                char ch = pattern.charAt(i);
                if (ch == '?') {
                    // Track the last wildcard position
                    lastWildcardPos = i;
                } else if (ch != '*') {
                    lastOccurrence[ch] = i;
                }
            }
        }
        
        // Second pass: calculate shifts using max(lastOccurrence, lastWildcardAfter)
        for (int ch = 0; ch < 128; ch++) {
            if (lastOccurrence[ch] != -1) {
                // Find if there's any '?' wildcard after this character's last occurrence
                int wildcardAfter = -1;
                for (int i = lastOccurrence[ch] + 1; i < m; i++) {
                    if (i < m - 1 && pattern.charAt(i) == '\\' && isEscapingBackslash(pattern, i)) {
                        i++; // Skip escaped character
                        continue;
                    }
                    if (pattern.charAt(i) == '?') {
                        wildcardAfter = i;
                        break; // We want the first '?' after the character, not the last
                    }
                }
                
                // Use your logic: m - max(lastOccurrence[ch], wildcardAfter)
                int maxPos = Math.max(lastOccurrence[ch], wildcardAfter);
                shift[ch] = m - maxPos;
            }
        }
        
        // Main search loop
        int i = 0;
        while (i <= n - m) {
            // Try to match pattern at current position
            if (matchesAtPosition(text, i, pattern)) {
                return true;
            }
            
            // Calculate shift using the character after current alignment
            if (i + m < n) {
                char nextChar = text.charAt(i + m);
                i += shift[nextChar];
            } else {
                break;
            }
        }
        
        return false;
    }
    
    // Helper method to check if pattern contains unescaped asterisk
    private static boolean containsUnescapedAsterisk(String pattern) {
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '*') {
                // Check if this asterisk is escaped
                if (i > 0 && pattern.charAt(i - 1) == '\\' && isEscapingBackslash(pattern, i - 1)) {
                    continue; // This asterisk is escaped
                }
                return true; // Found unescaped asterisk
            }
        }
        return false;
    }
    
    // Helper method to check if pattern matches at specific position in text
    private static boolean matchesAtPosition(String text, int textStart, String pattern) {
        int textPos = textStart;
        int patternPos = 0;
        
        while (patternPos < pattern.length() && textPos < text.length()) {
            // Handle escaped characters
            if (patternPos < pattern.length() - 1 && pattern.charAt(patternPos) == '\\' && 
                isEscapingBackslash(pattern, patternPos)) {
                char escapedChar = pattern.charAt(patternPos + 1);
                if (text.charAt(textPos) != escapedChar) {
                    return false;
                }
                patternPos += 2; // Skip both \ and escaped character
                textPos++;
                continue;
            }
            
            char patternChar = pattern.charAt(patternPos);
            
            // Handle '?' wildcard (matches any single character)
            if (patternChar == '?') {
                patternPos++;
                textPos++;
                continue;
            }
            
            // Handle regular character
            if (patternChar != text.charAt(textPos)) {
                return false;
            }
            
            patternPos++;
            textPos++;
        }
        
        // Check if we consumed the entire pattern
        return patternPos == pattern.length();
    }
    
    // Sunday algorithm implementation for patterns containing '*' wildcards
    private static boolean sundaySearchWithAsterisk(String text, String pattern) {
        int n = text.length();
        
        // For patterns with '*', we need a different approach
        // We'll use a modified Sunday where we try different alignment strategies
        
        // Split pattern by unescaped '*' to get segments
        java.util.List<String> segments = splitPatternByAsterisk(pattern);
        
        if (segments.isEmpty()) {
            return true; // Pattern was just "*" wildcards
        }
        
        // For single segment (no * wildcards), use regular Sunday
        if (segments.size() == 1) {
            return regularSundaySearch(text, segments.get(0));
        }
        
        // For multiple segments, we need to find them in order
        return findSegmentsInOrder(text, segments, pattern);
    }
    
    // Helper method to split pattern by unescaped asterisks
    private static java.util.List<String> splitPatternByAsterisk(String pattern) {
        java.util.List<String> segments = new java.util.ArrayList<>();
        StringBuilder currentSegment = new StringBuilder();
        
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '*') {
                // Check if this asterisk is escaped
                if (i > 0 && pattern.charAt(i - 1) == '\\' && isEscapingBackslash(pattern, i - 1)) {
                    // Escaped asterisk, add to current segment
                    currentSegment.append('*');
                } else {
                    // Unescaped asterisk, end current segment
                    if (currentSegment.length() > 0) {
                        segments.add(currentSegment.toString());
                        currentSegment = new StringBuilder();
                    }
                }
            } else {
                currentSegment.append(pattern.charAt(i));
            }
        }
        
        // Add final segment if exists
        if (currentSegment.length() > 0) {
            segments.add(currentSegment.toString());
        }
        
        return segments;
    }
    
    // Regular Sunday search for patterns without '*'
    private static boolean regularSundaySearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        
        if (m == 0) return true;
        if (n < m) return false;
        
        // Build shift table with wildcard logic
        int[] shift = new int[128];
        for (int i = 0; i < 128; i++) {
            shift[i] = m + 1;
        }
        
        // Calculate shifts with wildcard alignment logic
        int[] lastOccurrence = new int[128];
        for (int i = 0; i < 128; i++) {
            lastOccurrence[i] = -1;
        }
        
        // Fill positions
        for (int i = 0; i < m; i++) {
            if (i < m - 1 && pattern.charAt(i) == '\\' && isEscapingBackslash(pattern, i)) {
                char escapedChar = pattern.charAt(i + 1);
                lastOccurrence[escapedChar] = i;
                i++; // Skip escaped character
            } else {
                char ch = pattern.charAt(i);
                if (ch != '?' && ch != '*') {
                    lastOccurrence[ch] = i;
                }
            }
        }
        
        // Calculate shifts
        for (int ch = 0; ch < 128; ch++) {
            if (lastOccurrence[ch] != -1) {
                int wildcardAfter = -1;
                for (int i = lastOccurrence[ch] + 1; i < m; i++) {
                    if (i < m - 1 && pattern.charAt(i) == '\\' && isEscapingBackslash(pattern, i)) {
                        i++; continue;
                    }
                    if (pattern.charAt(i) == '?') {
                        wildcardAfter = i;
                        break;
                    }
                }
                int maxPos = Math.max(lastOccurrence[ch], wildcardAfter);
                shift[ch] = m - maxPos;
            }
        }
        
        // Main search
        int i = 0;
        while (i <= n - m) {
            if (matchesAtPosition(text, i, pattern)) {
                return true;
            }
            if (i + m < n) {
                char nextChar = text.charAt(i + m);
                i += shift[nextChar];
            } else {
                break;
            }
        }
        return false;
    }
    
    // Find all segments in order (simplified approach for * handling)
    private static boolean findSegmentsInOrder(String text, java.util.List<String> segments, String originalPattern) {
        // This is a simplified approach - in practice, this is very complex
        // We'll use a greedy approach with Sunday search for each segment
        
        int textPos = 0;
        boolean patternStartsWithAsterisk = originalPattern.startsWith("*") || 
            (originalPattern.length() > 1 && originalPattern.charAt(0) != '\\');
        boolean patternEndsWithAsterisk = originalPattern.endsWith("*") &&
            (originalPattern.length() < 2 || !isEscapingBackslash(originalPattern, originalPattern.length() - 2));
        
        for (int segmentIndex = 0; segmentIndex < segments.size(); segmentIndex++) {
            String segment = segments.get(segmentIndex);
            boolean found = false;
            
            // Search for this segment starting from current position
            for (int i = textPos; i <= text.length() - segment.length(); i++) {
                if (matchesAtPosition(text, i, segment)) {
                    textPos = i + segment.length();
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                return false;
            }
            
            // For the last segment, if pattern doesn't end with *, it must match at the end
            if (segmentIndex == segments.size() - 1 && !patternEndsWithAsterisk) {
                return textPos == text.length();
            }
        }
        
        return true;
    }
}