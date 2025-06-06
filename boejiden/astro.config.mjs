import { defineConfig } from "astro/config";

import tailwind from "@astrojs/tailwind";

// https://astro.build/config
export default defineConfig({
  markdown: {
    shikiConfig: {
      theme: "tokyo-night",
    },
    remarkPlugins: ["remark-math"],
    rehypePlugins: [["rehype-katex", {}]],
  },
  integrations: [tailwind()],
});
