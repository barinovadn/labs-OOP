const newman = require('newman');
const fs = require('fs');
const path = require('path');

const collectionPath = path.join(__dirname, 'API_Tests.postman_collection.json');
const environmentPath = path.join(__dirname, 'environment.json');
const resultsPath = path.join(__dirname, 'results', 'performance-results.json');
const tablePath = path.join(__dirname, 'results', 'PERFORMANCE_RESULTS.md');

const iterations = 10;
const results = {
    timestamp: new Date().toISOString(),
    branch: 'framework',
    iterations: iterations,
    endpoints: {}
};

function runNewmanTest() {
    return new Promise((resolve, reject) => {
        const startTime = Date.now();
        
        newman.run({
            collection: require(collectionPath),
            environment: require(environmentPath),
            iterationCount: 1,
            reporters: ['cli', 'json'],
            reporter: {
                json: {
                    export: resultsPath
                }
            }
        }, (err, summary) => {
            const endTime = Date.now();
            const duration = endTime - startTime;
            
            if (err) {
                reject(err);
            } else {
                resolve({
                    duration: duration,
                    summary: summary
                });
            }
        });
    });
}

async function measurePerformance() {
    console.log('Starting performance tests...');
    console.log(`Running ${iterations} iterations...\n`);
    
    const endpointTimes = {};
    
    for (let i = 0; i < iterations; i++) {
        console.log(`Iteration ${i + 1}/${iterations}...`);
        
        try {
            const result = await runNewmanTest();
            
            if (result.summary && result.summary.run && result.summary.run.executions) {
                result.summary.run.executions.forEach(execution => {
                    const requestName = execution.item.name;
                    const responseTime = execution.response ? execution.response.responseTime : 0;
                    
                    if (!endpointTimes[requestName]) {
                        endpointTimes[requestName] = [];
                    }
                    
                    endpointTimes[requestName].push(responseTime);
                });
            }
            
            console.log(`  Completed in ${result.duration}ms\n`);
        } catch (error) {
            console.error(`  Error in iteration ${i + 1}:`, error.message);
        }
    }
    
    Object.keys(endpointTimes).forEach(endpoint => {
        const times = endpointTimes[endpoint];
        const avg = times.reduce((a, b) => a + b, 0) / times.length;
        const min = Math.min(...times);
        const max = Math.max(...times);
        
        results.endpoints[endpoint] = {
            average: Math.round(avg),
            min: min,
            max: max,
            count: times.length
        };
    });
    
    const resultsDir = path.join(__dirname, 'results');
    if (!fs.existsSync(resultsDir)) {
        fs.mkdirSync(resultsDir, { recursive: true });
    }
    
    fs.writeFileSync(
        resultsPath,
        JSON.stringify(results, null, 2)
    );
    
    console.log('\nPerformance test completed!');
    console.log('Results saved to results/performance-results.json');
    
    generateMarkdownTable();
}

function generateMarkdownTable() {
    let markdown = '# Результаты теста производительности\n\n';
    markdown += `**Branch:** ${results.branch}<br>\n`;
    markdown += `**Timestamp:** ${results.timestamp}<br>\n`;
    markdown += `**Iterations:** ${results.iterations}<br>\n\n`;
    
    markdown += '## Производительность конечных точек\n\n';
    markdown += '| Конечная точка | Средняя (мс) | Минимальная (мс) | Максимальная (мс) | Запросов |\n';
    markdown += '|----------|--------------|----------|----------|----------|\n';
    
    Object.keys(results.endpoints).sort().forEach(endpoint => {
        const data = results.endpoints[endpoint];
        markdown += `| ${endpoint} | ${data.average} | ${data.min} | ${data.max} | ${data.count} |\n`;
    });
    
    fs.writeFileSync(tablePath, markdown);
    console.log('Markdown table saved to results/PERFORMANCE_RESULTS.md');
}

measurePerformance().catch(error => {
    console.error('Performance test failed:', error);
    process.exit(1);
});