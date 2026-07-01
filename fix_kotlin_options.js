const fs = require('fs');
const path = require('path');

const modules = [
    "core", "common", "designsystem", "data", "domain", "ai-engine", 
    "providers", "agents", "plugins", "features/chat", "features/workspace", 
    "features/models", "features/settings"
];

modules.forEach(mod => {
    const file = path.join(__dirname, mod, 'build.gradle.kts');
    let content = fs.readFileSync(file, 'utf8');
    content = content.replace(/    kotlinOptions \{[\s\S]*?jvmTarget = "17"[\s\S]*?\}/, '');
    fs.writeFileSync(file, content);
});
