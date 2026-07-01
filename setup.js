const fs = require('fs');
const path = require('path');

const modules = [
    "core", "common", "designsystem", "data", "domain", "ai-engine", 
    "providers", "agents", "plugins", "features/chat", "features/workspace", 
    "features/models", "features/settings"
];

modules.forEach(mod => {
    const dir = path.join(__dirname, mod);
    fs.mkdirSync(dir, { recursive: true });
    
    const ns = `com.aistudio.ultimate.${mod.replace(/\//g, '.').replace(/-/g, '')}`;
    const srcDir = path.join(dir, `src/main/java/com/aistudio/ultimate/${mod.replace(/\//g, '/').replace(/-/g, '')}`);
    fs.mkdirSync(srcDir, { recursive: true });

    const buildFile = path.join(dir, 'build.gradle.kts');
    if (mod === "domain") {
        fs.writeFileSync(buildFile, `plugins {
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "${ns}"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
}
dependencies {
    implementation(libs.kotlinx.coroutines.core)
}`);
    } else {
        fs.writeFileSync(buildFile, `plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}
android {
    namespace = "${ns}"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
}`);
    }
});

const settingsFile = path.join(__dirname, 'settings.gradle.kts');
let settingsContent = fs.readFileSync(settingsFile, 'utf8');

modules.forEach(mod => {
    const includeName = mod.replace(/\//g, ':');
    if (!settingsContent.includes(`include(":${includeName}")`)) {
        settingsContent += `\ninclude(":${includeName}")`;
    }
});

fs.writeFileSync(settingsFile, settingsContent);
console.log("Modules created successfully!");
