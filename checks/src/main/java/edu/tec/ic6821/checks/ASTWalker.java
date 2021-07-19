package edu.tec.ic6821.checks;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ASTWalker {

    private static final String TYPE_COMMA_REPLACEMENT = "~";

    private final DetailAST ast;

    public ASTWalker(final DetailAST ast) {
        this.ast = ast;
    }

    public List<DetailAST> filterTokensByType(final int tokenType) {
        final List<DetailAST> tokens = new ArrayList<>();
        if (this.ast != null) {
            DetailAST child = this.ast.getFirstChild();
            while (child != null) {
                if (child.getType() == tokenType) {
                    tokens.add(child);
                }
                child = child.getNextSibling();
            }
        }

        return tokens;
    }

    public String flattenType() {
        if (this.ast.getType() != TokenTypes.TYPE) {
            return null;
        }

        final StringBuilder builder = new StringBuilder();

        final class Recursive<F> {
            F lambda;
        }

        final Recursive<Consumer<DetailAST>> traverse = new Recursive<>();
        traverse.lambda = (DetailAST node) -> {
            if (!node.hasChildren()) {
                final String text = node.getType() == TokenTypes.COMMA ? TYPE_COMMA_REPLACEMENT : node.getText();
                builder.append(text);
            } else {
                // these are non-leaf nodes linked to literal tokens
                if (node.getType() == TokenTypes.TYPE_UPPER_BOUNDS
                        || node.getType() == TokenTypes.TYPE_LOWER_BOUNDS
                        || node.getType() == TokenTypes.ARRAY_DECLARATOR) {
                    builder.append(node.getText());
                }
                DetailAST child = node.getFirstChild();
                while (child != null) {
                    traverse.lambda.accept(child);
                    child = child.getNextSibling();
                }
            }
        };

        traverse.lambda.accept(this.ast);
        return builder.toString();
    }

    public DetailAST findPackageDef() {
        DetailAST sibling = this.ast.getPreviousSibling();
        while (sibling != null) {
            if (sibling.getType() == TokenTypes.PACKAGE_DEF) {
                return sibling;
            }
            sibling = sibling.getPreviousSibling();
        }

        return null;
    }

}
