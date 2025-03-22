import { defineCollection, z } from "astro:content";

const solutionCollection = defineCollection({
  type: "content",
  schema: z.object({
    title: z.string(),
    year: z.enum([
      "2015",
      "2016",
      "2017",
      "2018",
      "2019",
      "2020",
      "2021",
      "2022",
      "2023",
      "2024",
    ]),
    month: z.enum(["December", "January", "February", "US Open"]),
    division: z.enum(["Bronze", "Silver", "Gold", "Platinum"]),
    index: z.number(),
  }),
});

export const collections = {
  solutions: solutionCollection,
};
