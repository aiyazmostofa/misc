import { defineConfig } from 'astro/config';
import theme from './theme.json';

// https://astro.build/config
export default defineConfig({
    markdown: {
        shikiConfig: {
            theme: theme,
        },
        remarkPlugins: [
            'remark-math',
        ],
        rehypePlugins: [
            ['rehype-katex', {

            }]
        ]
    }
});