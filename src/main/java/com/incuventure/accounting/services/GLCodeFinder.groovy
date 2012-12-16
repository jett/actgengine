package com.incuventure.accounting.services

import com.incuventure.accounting.GLReference


class GLCodeFinder {

    def reference = [
        productType: "LC",
    ]

    public static GLReference getGLReference(String productType) {
        return new GLReference("12112121", "121212121C", "PHP", "Contingent Liability");
    }

}
