# -*- coding: utf-8 -*-

from .context import pet_race_job

import unittest


class AdvancedTestSuite(unittest.TestCase):
    """Advanced test cases."""

    def test_thoughts(self):
        pet_race_job.hmm()


if __name__ == '__main__':
    unittest.main()
