package edu.tec.ic6821.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StructureCheck extends AbstractCheck {

    private static class Value {
        final String name;
        final String type;
        final String spec;

        Value(String spec) {
            if (spec == null || spec.isBlank()) {
                throw new IllegalArgumentException("Can't parse Value with empty or null spec");
            }

            final String[] tokens = spec.split(":");
            if (tokens.length != 2) {
                throw new IllegalArgumentException(String.format("Illegal spec format for Value %s", spec));
            }

            this.name = tokens[0];
            this.type = tokens[1];
            this.spec = spec;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Value value = (Value) o;
            return name.equals(value.name) && type.equals(value.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, type);
        }

        @Override
        public String toString() {
            return spec.replace('~', ',');
        }
    }

    private static class Signature {
        final String name;
        final String type;
        final List<Value> params;
        final String spec;

        public Signature(final String spec) {
            if (spec == null || spec.isBlank()) {
                throw new IllegalArgumentException("Can't parse Value with empty or null spec");
            }

            this.spec = spec;

            final int delim = spec.lastIndexOf(':');
            if (delim == -1) {
                throw new IllegalArgumentException(String.format("Illegal spec format for Signature %s", spec));
            }
            final String[] tokens = {spec.substring(0, delim), spec.substring(delim + 1)};
            if (tokens[0].isBlank()) {
                throw new IllegalArgumentException(String.format("Illegal spec format for Signature %s", spec));
            }

            this.type = tokens[1];

            final int openingPar = tokens[0].indexOf('(');
            final int closingPar = tokens[0].lastIndexOf(')');
            final boolean hasParams = openingPar > -1 && closingPar > -1;
            final boolean malformedParams = openingPar > -1 ^ closingPar > -1;
            if (hasParams) {
                this.name = tokens[0].substring(0, openingPar);
                this.params = new ArrayList<>();
                final String paramsSpec = tokens[0].substring(openingPar + 1, closingPar);
                if (!paramsSpec.isBlank()) {
                    final String[] paramTokens = paramsSpec.split(";");
                    for (final String paramToken : paramTokens) {
                        params.add(new Value(paramToken));
                    }
                }
            } else if (malformedParams) {
                throw new IllegalArgumentException(String.format("Illegal spec format for Signature %s", spec));
            } else {
                this.name = tokens[0];
                this.params = Collections.emptyList();
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Signature signature = (Signature) o;
            return spec.equals(signature.spec);
        }

        @Override
        public int hashCode() {
            return Objects.hash(spec);
        }

        @Override
        public String toString() {
            return this.spec.replace('~', ',');
        }
    }

    private static final String ABSTRACTION_CLASS = "class";
    private static final String ABSTRACTION_INTERFACE = "interface";
    private static final String ABSTRACTION_ENUM = "enum";

    private static final String MSG_WRONG_ABSTRACTION_TYPE = "[IC-6821] La abstracción %s debería ser de tipo %s";
    private static final String MSG_WRONG_PACKAGE = "[IC-6821] %s %s debería estar en el paquete %s";
    private static final String MSG_MISSING_IMPLEMENTS = "[IC-6821] %s %s debería implementar %s";
    private static final String MSG_MISSING_PRIVATE_FIELD = "[IC-6821] %s %s debería contener el campo privado %s";
    private static final String MSG_MISSING_PUBLIC_CONSTRUCTOR =
            "[IC-6821] %s %s debería contener el constructor público %s";
    private static final String MSG_MISSING_PRIVATE_CONSTRUCTOR =
            "[IC-6821] %s %s debería contener el constructor privado %s";
    private static final String MSG_MISSING_PUBLIC_STATIC_METHOD =
            "[IC-6821] %s %s debería contener el método público estático %s";
    private static final String MSG_MISSING_PRIVATE_STATIC_METHOD =
            "[IC-6821] %s %s debería contener el método privado estático %s";
    private static final String MSG_MISSING_PUBLIC_METHOD = "[IC-6821] %s %s debería contener el método público %s";
    private static final String MSG_MISSING_INTERFACE_METHOD = "[IC-6821] %s %s debería contener el método %s";
    private static final String MSG_MISSING_PRIVATE_METHOD = "[IC-6821] %s %s debería contener el método privado %s";
    private static final String MSG_MISSING_ENUM_VALUE = "[IC-6821] %s %s debería definir el valor %s";

    private String name;
    private String packageName;
    private String abstractionType;
    private final List<String> implementsInterfaces = new ArrayList<>();
    private final List<String> privateFields = new ArrayList<>();
    private final List<String> privateConstructors = new ArrayList<>();
    private final List<String> publicConstructors = new ArrayList<>();
    private final List<String> publicStaticMethods = new ArrayList<>();
    private final List<String> privateStaticMethods = new ArrayList<>();
    private final List<String> publicMethods = new ArrayList<>();
    private final List<String> interfaceMethods = new ArrayList<>();
    private final List<String> privateMethods = new ArrayList<>();
    private final List<String> enumValues = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setAbstractionType(String abstractionType) {
        this.abstractionType = abstractionType;
    }

    public void setImplementsInterfaces(String... implementsInterfaces) {
        this.implementsInterfaces.addAll(Arrays.asList(implementsInterfaces));
    }

    public void setPublicStaticMethods(String... publicStaticMethods) {
        this.publicStaticMethods.addAll(Arrays.asList(publicStaticMethods));
    }

    public void setPrivateStaticMethods(String... privateStaticMethods) {
        this.privateStaticMethods.addAll(Arrays.asList(privateStaticMethods));
    }

    public void setPublicConstructors(String... publicConstructors) {
        this.publicConstructors.addAll(Arrays.asList(publicConstructors));
    }

    public void setPrivateConstructors(String... privateConstructors) {
        this.privateConstructors.addAll(Arrays.asList(privateConstructors));
    }

    public void setPrivateFields(String... privateFields) {
        this.privateFields.addAll(Arrays.asList(privateFields));
    }

    public void setPublicMethods(String... publicMethods) {
        this.publicMethods.addAll(Arrays.asList(publicMethods));
    }

    public void setInterfaceMethods(String... interfaceMethods) {
        this.interfaceMethods.addAll(Arrays.asList(interfaceMethods));
    }

    public void setPrivateMethods(String... privateMethods) {
        this.privateMethods.addAll(Arrays.asList(privateMethods));
    }

    public void setEnumValues(String... enumValues) {
        this.enumValues.addAll(Arrays.asList(enumValues));
    }

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.CLASS_DEF, TokenTypes.ENUM_DEF, TokenTypes.INTERFACE_DEF};
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return getDefaultTokens();
    }

    @Override
    public void visitToken(DetailAST ast) {
        super.visitToken(ast);

        if (this.name == null || this.name.isBlank()) {
            throw new IllegalArgumentException("Property name is required");
        }

        final DetailAST ident = ast.findFirstToken(TokenTypes.IDENT);
        if (!this.name.equals(ident.getText())) {
            return;
        }

        checkAbstractionType(ast);
        checkPackage(ast);
        checkImplementsInterfaces(ast);
        checkPrivateFields(ast);
        checkPublicConstructors(ast);
        checkPrivateConstructors(ast);
        checkPublicStaticMethods(ast);
        checkPrivateStaticMethods(ast);
        checkPublicMethods(ast);
        checkInterfaceMethods(ast);
        checkPrivateMethods(ast);
        checkEnumValues(ast);
    }

    private void checkAbstractionType(DetailAST ast) {
        if (this.abstractionType == null || this.abstractionType.isBlank()) {
            return;
        }

        final int requiredType;
        switch (this.abstractionType) {
            case ABSTRACTION_CLASS:
                requiredType = TokenTypes.CLASS_DEF;
                break;
            case ABSTRACTION_INTERFACE:
                requiredType = TokenTypes.INTERFACE_DEF;
                break;
            case ABSTRACTION_ENUM:
                requiredType = TokenTypes.ENUM_DEF;
                break;
            default:
                throw new IllegalArgumentException("Unknown abstraction type, expected [class|interface|enum] but got " + this.abstractionType);
        }

        if (ast.getType() != requiredType) {
            log(ast.getLineNo(), String.format(MSG_WRONG_ABSTRACTION_TYPE, this.name, this.abstractionType));
        }
    }

    private void checkPackage(DetailAST ast) {
        if (this.packageName == null || this.packageName.isBlank()) {
            return;
        }
        final DetailAST packageDef = new ASTWalker(ast).findPackageDef();
        if (packageDef != null) {
            final DetailAST dot = packageDef.findFirstToken(TokenTypes.DOT);
            if (dot != null) {
                final DetailAST ident = dot.findFirstToken(TokenTypes.IDENT);
                if (this.packageName.equals(ident.getText())) {
                    return;
                }
            }
        }

        log(ast.getLineNo(), String.format(MSG_WRONG_PACKAGE, this.abstractionType, this.name, this.packageName));
    }

    private void checkImplementsInterfaces(final DetailAST ast) {
        if (this.implementsInterfaces.isEmpty()) {
            return;
        }

        final DetailAST implementsClause = ast.findFirstToken(TokenTypes.IMPLEMENTS_CLAUSE);
        if (implementsClause == null) {
            log(ast.getLineNo(),
                    String.format(MSG_MISSING_IMPLEMENTS, this.abstractionType, this.name, this.implementsInterfaces));
        }

        final List<String> idents = new ASTWalker(implementsClause)
                .filterTokensByType(TokenTypes.IDENT).stream()
                .map(DetailAST::getText)
                .collect(Collectors.toList());

        final List<String> missingInterfaces = this.implementsInterfaces.stream()
                .filter(inter -> !idents.contains(inter))
                .collect(Collectors.toList());

        missingInterfaces.forEach(missing ->
                log(ast.getLineNo(), String.format(MSG_MISSING_IMPLEMENTS, this.abstractionType, this.name, missing))
        );
    }

    private void checkPrivateFields(DetailAST ast) {
        if (this.privateFields.isEmpty()) {
            return;
        }

        final List<Value> fields = this.privateFields.stream()
                .map(Value::new)
                .collect(Collectors.toList());

        final DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);
        if (objBlock != null) {
            final List<DetailAST> variableDefs = new ASTWalker(objBlock).filterTokensByType(TokenTypes.VARIABLE_DEF);
            variableDefs.forEach(variableDef -> {
                final Value matchedValue = matchDefToValues(variableDef, fields, TokenTypes.LITERAL_PRIVATE);
                if (matchedValue != null) {
                    fields.remove(matchedValue);
                }
            });
        }

        fields.forEach(privateField -> log(ast.getLineNo(),
                String.format(MSG_MISSING_PRIVATE_FIELD, this.abstractionType, this.name, privateField)));
    }

    private void checkPublicConstructors(final DetailAST ast) {
        if (this.publicConstructors.isEmpty()) {
            return;
        }

        checkConstructors(ast, this.publicConstructors, MSG_MISSING_PUBLIC_CONSTRUCTOR, TokenTypes.LITERAL_PUBLIC);
    }

    private void checkPrivateConstructors(final DetailAST ast) {
        if (this.privateConstructors.isEmpty()) {
            return;
        }

        checkConstructors(ast, this.privateConstructors, MSG_MISSING_PRIVATE_CONSTRUCTOR, TokenTypes.LITERAL_PRIVATE);
    }

    private void checkConstructors(final DetailAST ast, final List<String> constructors,
                                   final String msg, final int... visibilityModifiers) {

        final List<Signature> signatures = constructors.stream()
                .map(signature -> this.name + signature + ":" + this.name)
                .map(Signature::new)
                .collect(Collectors.toList());

        final DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);
        if (objBlock != null) {
            final List<DetailAST> ctorDefs = new ASTWalker(objBlock).filterTokensByType(TokenTypes.CTOR_DEF);
            ctorDefs.forEach(ctorDef -> {
                final Signature matchedSignature = matchCtorDefToSignatures(ctorDef, signatures, visibilityModifiers);
                if (matchedSignature != null) {
                    signatures.remove(matchedSignature);
                }
            });
        }

        signatures.forEach(constructor -> log(ast.getLineNo(),
                String.format(msg, this.abstractionType, this.name, constructor)));
    }

    private void checkPublicStaticMethods(final DetailAST ast) {
        if (this.publicStaticMethods.isEmpty()) {
            return;
        }

        checkMethods(ast, this.publicStaticMethods, MSG_MISSING_PUBLIC_STATIC_METHOD,
                TokenTypes.LITERAL_PUBLIC, TokenTypes.LITERAL_STATIC);
    }

    private void checkPrivateStaticMethods(final DetailAST ast) {
        if (this.privateStaticMethods.isEmpty()) {
            return;
        }

        checkMethods(ast, this.privateStaticMethods, MSG_MISSING_PRIVATE_STATIC_METHOD,
                TokenTypes.LITERAL_PRIVATE, TokenTypes.LITERAL_STATIC);
    }

    private void checkPublicMethods(final DetailAST ast) {
        if (this.publicMethods.isEmpty()) {
            return;
        }

        checkMethods(ast, this.publicMethods, MSG_MISSING_PUBLIC_METHOD, TokenTypes.LITERAL_PUBLIC);
    }

    private void checkInterfaceMethods(final DetailAST ast) {
        if (this.interfaceMethods.isEmpty()) {
            return;
        }

        checkMethods(ast, this.interfaceMethods, MSG_MISSING_INTERFACE_METHOD);
    }

    private void checkPrivateMethods(final DetailAST ast) {
        if (this.privateMethods.isEmpty()) {
            return;
        }

        checkMethods(ast, this.privateMethods, MSG_MISSING_PRIVATE_METHOD, TokenTypes.LITERAL_PRIVATE);
    }

    private void checkMethods(DetailAST ast, List<String> methods, String msg, int... visibilityModifiers) {
        final List<Signature> signatures = methods.stream()
                .map(Signature::new)
                .collect(Collectors.toList());

        final DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);
        if (objBlock != null) {
            final List<DetailAST> methodDefs = new ASTWalker(objBlock).filterTokensByType(TokenTypes.METHOD_DEF);
            methodDefs.forEach(methodDef -> {
                final Signature matchedSignature = matchDefToSignatures(methodDef, signatures, visibilityModifiers);
                if (matchedSignature != null) {
                    signatures.remove(matchedSignature);
                }
            });
        }

        signatures.forEach(signature -> log(ast.getLineNo(),
                String.format(msg, this.abstractionType, this.name, signature)));
    }

    private void checkEnumValues(final DetailAST ast) {
        if (this.enumValues.isEmpty()) {
            return;
        }

        final List<String> values = new ArrayList<>(this.enumValues);

        final DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);
        if (objBlock != null) {
            final List<DetailAST> enumConstantDefs = new ASTWalker(objBlock).filterTokensByType(TokenTypes.ENUM_CONSTANT_DEF);
            enumConstantDefs.forEach(enumConstantDef -> {
                final DetailAST ident = enumConstantDef.findFirstToken(TokenTypes.IDENT);
                values.remove(ident.getText());
            });
        }

        values.forEach(enumValue -> log(ast.getLineNo(),
                String.format(MSG_MISSING_ENUM_VALUE, this.abstractionType, this.name, enumValue)));

    }

    private Signature matchDefToSignatures(final DetailAST def,
                                           final List<Signature> signatures,
                                           final int... visibilityModifiers) {
        if (!validateVisibilityModifiers(def, visibilityModifiers)) {
            return null;
        }

        final DetailAST ident = def.findFirstToken(TokenTypes.IDENT);
        final DetailAST type = def.findFirstToken(TokenTypes.TYPE);

        final List<Signature> nameMatched = signatures.stream()
                // support both :type and name:type specs
                .filter(signature -> signature.name.isBlank() || signature.name.equals(ident.getText()))
                .collect(Collectors.toList());

        final List<Signature> typeMatched = nameMatched.stream()
                .filter(signature -> signature.type.equals(new ASTWalker(type).flattenType()))
                .collect(Collectors.toList());

        final DetailAST parameters = def.findFirstToken(TokenTypes.PARAMETERS);
        final List<DetailAST> parameterDefs = new ASTWalker(parameters).filterTokensByType(TokenTypes.PARAMETER_DEF);
        final List<Signature> paramsMatched = typeMatched.stream()
                .filter(signature -> matchParameterDefsToSignatureParams(parameterDefs, signature))
                .collect(Collectors.toList());

        return paramsMatched.isEmpty() ? null : paramsMatched.get(0);
    }

    private Signature matchCtorDefToSignatures(final DetailAST ctorDef, final List<Signature> signatures, final int... visibilityModifiers) {
        if (!validateVisibilityModifiers(ctorDef, visibilityModifiers)) {
            return null;
        }

        final DetailAST parameters = ctorDef.findFirstToken(TokenTypes.PARAMETERS);
        final List<DetailAST> parameterDefs = new ASTWalker(parameters).filterTokensByType(TokenTypes.PARAMETER_DEF);
        final List<Signature> paramsMatched = signatures.stream()
                .filter(signature -> matchParameterDefsToSignatureParams(parameterDefs, signature))
                .collect(Collectors.toList());

        return paramsMatched.isEmpty() ? null : paramsMatched.get(0);
    }

    private Value matchDefToValues(final DetailAST def, final List<Value> values, final int... visibilityModifiers) {
        if (!validateVisibilityModifiers(def, visibilityModifiers)) {
            return null;
        }

        final DetailAST ident = def.findFirstToken(TokenTypes.IDENT);
        final DetailAST type = def.findFirstToken(TokenTypes.TYPE);

        final List<Value> nameMatched = values.stream()
                // support both :type and name:type specs
                .filter(value -> value.name.isBlank() || value.name.equals(ident.getText()))
                .collect(Collectors.toList());

        final List<Value> typeMatched = nameMatched.stream()
                .filter(value -> value.type.equals(new ASTWalker(type).flattenType()))
                .collect(Collectors.toList());

        return typeMatched.isEmpty() ? null : typeMatched.get(0);
    }

    private boolean matchParameterDefsToSignatureParams(final List<DetailAST> parameterDefs, final Signature signature) {
        if (parameterDefs.size() != signature.params.size()) {
            return false;
        }

        final Iterator<DetailAST> parameterDefsIter = parameterDefs.iterator();
        final Iterator<Value> signatureParamsIter = signature.params.iterator();

        while (parameterDefsIter.hasNext() && signatureParamsIter.hasNext()) {
            final DetailAST parameterDef = parameterDefsIter.next();
            final Value value = signatureParamsIter.next();
            if (matchDefToValues(parameterDef, Collections.singletonList(value)) == null) {
                return false;
            }
        }

        return true;
    }

    private boolean validateVisibilityModifiers(final DetailAST def, final int[] visibilityModifiers) {
        if (visibilityModifiers.length > 0) {
            final DetailAST modifiers = def.findFirstToken(TokenTypes.MODIFIERS);
            if (modifiers == null) {
                return false;
            }
            for (final int visibilityModifier : visibilityModifiers) {
                if (modifiers.findFirstToken(visibilityModifier) == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
