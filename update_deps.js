const fs = require('fs');
const path = require('path');

const addDep = (mod, dep) => {
    const file = path.join(__dirname, mod, 'build.gradle.kts');
    let content = fs.readFileSync(file, 'utf8');
    if (!content.includes(dep)) {
        content = content.replace('dependencies {', `dependencies {\n    implementation(project("${dep}"))`);
        fs.writeFileSync(file, content);
    }
};

addDep('ai-engine', ':domain');
addDep('providers', ':domain');
addDep('agents', ':domain');
addDep('agents', ':ai-engine');
addDep('data', ':domain');
addDep('features/chat', ':domain');
addDep('features/chat', ':ai-engine');
addDep('features/chat', ':common');
addDep('features/chat', ':designsystem');
addDep('features/workspace', ':domain');
addDep('features/workspace', ':ai-engine');
addDep('features/models', ':domain');
addDep('features/settings', ':domain');
